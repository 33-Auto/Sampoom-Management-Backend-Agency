package com.sampoom.backend.api.outbound.service;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.outbound.dto.AgencyOutboundRequestDTO;
import com.sampoom.backend.api.outbound.dto.AgencyOutboundResponseDTO;
import com.sampoom.backend.api.outbound.entity.AgencyOutboundItem;
import com.sampoom.backend.api.outbound.repository.AgencyOutboundItemRepository;
import com.sampoom.backend.api.outbound.repository.AgencyOutboundQueryRepository;
import com.sampoom.backend.api.partread.entity.Part;
import com.sampoom.backend.api.partread.service.PartReadService;
import com.sampoom.backend.api.stock.service.StockService;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AgencyOutboundService {

    private final AgencyOutboundItemRepository outboundRepository;
    private final AgencyOutboundQueryRepository outboundQueryRepository;
    private final AgencyRepository agencyRepository;
    private final PartReadService partReadService;
    private final StockService stockService;

    // 출고 목록에 부품 추가
    @Transactional
    public void addItem(Long agencyId, AgencyOutboundRequestDTO request) {
        Agency agency = validateAgency(agencyId);
        Part part = partReadService.getPartById(request.getPartId());

        // 재고 조회 (StockService 호출)
        Map<Long, Integer> stockMap = stockService.getStockByAgency(agencyId);
        int currentStock = stockMap.getOrDefault(part.getId(), 0);

        // 현재 출고 목록에 있는 해당 부품의 총 수량 계산
        Long totalOutboundQuantity = outboundRepository.getTotalQuantityByAgencyAndPart(agencyId, part.getId());
        int reserved = totalOutboundQuantity == null ? 0 : totalOutboundQuantity.intValue();

        // 사용 가능한 재고 = 실제 재고 - 이미 출고 목록에 담긴 수량
        int availableStock = currentStock - reserved;

        // 재고 부족 시 추가 금지
        if (availableStock < request.getQuantity()) {
            throw new BadRequestException(ErrorStatus.STOCK_INSUFFICIENT);
        }

        // 기존 출고 항목 있는지 확인
        outboundRepository.findByAgency_IdAndPartId(agencyId, request.getPartId())
                .ifPresentOrElse(
                        item -> {
                            int newQuantity = item.getQuantity() + request.getQuantity();

                            // 추가할 때도 재고 초과 방지
                            if (newQuantity > currentStock) {
                                throw new BadRequestException(ErrorStatus.STOCK_INSUFFICIENT);
                            }
                            item.updateQuantity(newQuantity);
                        },
                        () -> outboundRepository.save(AgencyOutboundItem.create(agency, part, request.getQuantity()))
                );
    }

    // 출고 목록 조회
    @Transactional
    public List<AgencyOutboundResponseDTO> getOutboundItems(Long agencyId) {
        agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        return outboundQueryRepository.findOutboundItemsWithNames(agencyId);
    }

    // 출고 수량 변경
    @Transactional
    public void updateQuantity(Long agencyId, Long itemId, int newQuantity) {
        AgencyOutboundItem item = outboundRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.OUTBOUND_ITEM_NOT_FOUND));

        if (!item.getAgency().getId().equals(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND);
        }

        // 재고 검증
        Map<Long, Integer> stockMap = stockService.getStockByAgency(agencyId);
        int currentStock = stockMap.getOrDefault(item.getPartId(), 0);

        Long totalReserved = outboundRepository.getTotalQuantityByAgencyAndPart(agencyId, item.getPartId());
        int reservedExcludingCurrent = (totalReserved == null ? 0 : totalReserved.intValue()) - item.getQuantity();

        if (newQuantity + reservedExcludingCurrent  > currentStock) {
            throw new BadRequestException(ErrorStatus.STOCK_INSUFFICIENT);
        }

        item.updateQuantity(newQuantity);
    }

    // 출고 항목 삭제
    @Transactional
    public void deleteItem(Long agencyId, Long itemId) {
        AgencyOutboundItem item = outboundRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.OUTBOUND_ITEM_NOT_FOUND));

        if (!item.getAgency().getId().equals(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND);
        }

        outboundRepository.delete(item);
    }

    // 출고 처리 → 실제 재고 차감 + 목록 비우기
    @Transactional
    public void processOutbound(Long agencyId) {
        List<AgencyOutboundItem> items = outboundRepository.findByAgency_Id(agencyId);

        for (AgencyOutboundItem item : items) {
            stockService.decreaseStock(agencyId, item.getPartId(), item.getQuantity());
        }

        outboundRepository.deleteAll(items);
    }

    private Agency validateAgency(Long agencyId) {
        return agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));
    }
}