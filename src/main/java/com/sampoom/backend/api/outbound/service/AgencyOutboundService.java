package com.sampoom.backend.api.outbound.service;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.outbound.dto.AgencyOutboundRequestDTO;
import com.sampoom.backend.api.outbound.dto.AgencyOutboundResponseDTO;
import com.sampoom.backend.api.outbound.dto.AgencyOutboundUpdateRequestDTO;
import com.sampoom.backend.api.outbound.entity.AgencyOutboundItem;
import com.sampoom.backend.api.outbound.repository.AgencyOutboundItemRepository;
import com.sampoom.backend.api.outbound.repository.AgencyOutboundQueryRepository;
import com.sampoom.backend.api.part.entity.Part;
import com.sampoom.backend.api.part.service.PartReadService;
import com.sampoom.backend.api.stock.service.StockService;
import com.sampoom.backend.common.dto.CategoryResponseDTO;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.mapper.ResponseMapper;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgencyOutboundService {

    private final AgencyOutboundItemRepository outboundRepository;
    private final AgencyOutboundQueryRepository outboundQueryRepository;
    private final AgencyRepository agencyRepository;
    private final PartReadService partReadService;
    private final StockService stockService;
    private final ResponseMapper responseMapper;

    // 개인별 출고목록에 부품 추가 (JWT userId 사용)
    @Transactional
    public void addItem(Long agencyId, String userId, AgencyOutboundRequestDTO request) {
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        Part part = partReadService.getPartById(request.getPartId());

        // 해당 사용자의 기존 출고목록 항목 확인 (userId 기준)
        AgencyOutboundItem existingItem = outboundRepository
                .findByUserIdAndPartId(userId, request.getPartId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.updateQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            AgencyOutboundItem newItem = AgencyOutboundItem.create(
                    agency, userId, part, request.getQuantity()
            );
            outboundRepository.save(newItem);
        }
    }

    // 개인별 출고목록 조회 (JWT userId 사용)
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getOutboundItems(Long agencyId, String userId) {
        if (!agencyRepository.existsById(agencyId)) {
            throw new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND);
        }

        // 해당 사용자의 출고목록 항목만 조회 (userId 기준)
        List<AgencyOutboundResponseDTO> items = outboundQueryRepository.findOutboundItemsByUserId(userId);

        // flat → nested 구조 변환
        return responseMapper.toNestedStructure(items);
    }

    // 개인별 출고목록 수량 수정 (JWT userId 사용)
    @Transactional
    public void updateQuantity(Long agencyId, Long outboundId, String userId, int quantity) {
        AgencyOutboundItem item = outboundRepository.findById(outboundId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.OUTBOUND_ITEM_NOT_FOUND));

        // 해당 아이템이 이 사용자의 것인지 확인
        if (!item.getUserId().equals(userId)) {
            throw new BadRequestException(ErrorStatus.ACCESS_DENIED);
        }

        item.updateQuantity(quantity);
    }

    // 개인별 출고목록 항목 삭제 (JWT userId 사용)
    @Transactional
    public void deleteItem(Long agencyId, Long outboundId, String userId) {
        AgencyOutboundItem item = outboundRepository.findById(outboundId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.OUTBOUND_ITEM_NOT_FOUND));

        // 해당 아이템이 이 사용자의 것인지 확인
        if (!item.getUserId().equals(userId)) {
            throw new BadRequestException(ErrorStatus.ACCESS_DENIED);
        }

        outboundRepository.delete(item);
    }

    // 개인별 출고 처리 (JWT userId 사용) - 재고에서 차감하고 출고목록 초기화
    @Transactional
    public void processOutbound(Long agencyId, String userId) {
        List<AgencyOutboundItem> outboundItems = outboundRepository.findByUserId(userId);

        if (outboundItems.isEmpty()) {
            throw new BadRequestException(ErrorStatus.OUTBOUND_LIST_EMPTY);
        }

        // 각 아이템에 대해 재고 차감
        for (AgencyOutboundItem item : outboundItems) {
            stockService.decreaseStock(agencyId, item.getPartId(), item.getQuantity());
        }

        // 출고목록 전체 삭제
        outboundRepository.deleteAllByUserId(userId);
    }
}
