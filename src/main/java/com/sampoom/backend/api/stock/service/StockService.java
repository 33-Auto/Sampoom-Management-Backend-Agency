package com.sampoom.backend.api.stock.service;

import com.sampoom.backend.api.part.feign.PartClient;
import com.sampoom.backend.api.part.feign.dto.PartResponseDTO;
import com.sampoom.backend.api.stock.dto.StockInboundRequestDTO;
import com.sampoom.backend.api.stock.dto.StockOutboundRequestDTO;
import com.sampoom.backend.api.stock.dto.StockResponseDTO;
import com.sampoom.backend.api.stock.entity.AgencyStock;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

//    private final AgencyStockRepository agencyStockRepository;
//    private final AgencyRepository agencyRepository;
    private final PartClient partClient;

    // DB 대신 사용할 mock 데이터 저장소
    private final Map<Long, AgencyStock> mockStockDB = new HashMap<>();

    // 서버 시작 시 기본 mock 데이터 5개 생성
    @PostConstruct
    public void initMockData() {
        for (long i = 1; i <= 10; i++) {
            mockStockDB.put(i, AgencyStock.builder()
                    .id(i)
                    .agencyId(1L)
                    .partId(i)
                    .quantity(100 + (int) i)
                    .build());
        }
    }

    // 재고 목록 조회
    @Transactional
    public List<StockResponseDTO> getStockList(Long agencyId) {

        // 임시 부품 리스트 저장
        List<PartResponseDTO> allParts = new ArrayList<>();

        // groupId 여러 개 돌려서 부품 데이터 긁어오기 (임시)
        for (long groupId = 1; groupId <= 5; groupId++) {
            try {
                allParts.addAll(partClient.getPartsByGroup(groupId).getData());
            } catch (Exception e) {
                log.warn("Group {} 조회 실패: {}", groupId, e.getMessage());
            }
        }

        // Map으로 변환해 빠르게 partId 매칭
        Map<Long, PartResponseDTO> partMap = allParts.stream()
                .collect(Collectors.toMap(PartResponseDTO::getPartId, p -> p, (a, b) -> a));

        // mockStockDB 기준으로 재고 + 부품 정보 합침
        return mockStockDB.values().stream()
                .filter(stock -> stock.getAgencyId().equals(agencyId))
                .map(stock -> {
                    PartResponseDTO part = partMap.get(stock.getPartId());
                    return StockResponseDTO.builder()
                            .stockId(stock.getId())
                            .agencyId(stock.getAgencyId())
                            .partId(stock.getPartId())
                            .quantity(stock.getQuantity())
                            .partName(part != null ? part.getName() : "N/A")
                            .partCode(part != null ? part.getCode() : "UNKNOWN")
                            .build();
                })
                .toList();
    }

    // 재고 입고 처리
    @Transactional
    public void inboundStock(Long agencyId, StockInboundRequestDTO stockInboundRequestDTO) {

        for (StockInboundRequestDTO.InboundItem item : stockInboundRequestDTO.getItems()) {

            Optional<AgencyStock> existingStock = mockStockDB.values().stream()
                    .filter(s -> s.getAgencyId().equals(agencyId) && s.getPartId().equals(item.getPartId()))
                    .findFirst();

            if (existingStock.isPresent()) {
                existingStock.get().increaseQuantity(item.getQuantity());
            } else {
                long newId = mockStockDB.size() + 1L;
                mockStockDB.put(newId, AgencyStock.builder()
                        .id(newId)
                        .agencyId(agencyId)
                        .partId(item.getPartId())
                        .quantity(item.getQuantity())
                        .build());
            }
        }

//        // 대리점 존재 여부 확인
//        Agency agency = agencyRepository.findById(agencyId)
//                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND.getMessage()));
//
//        for (StockInboundRequestDTO.InboundItem item : stockInboundRequestDTO.getItems()) {
//            // 해당 대리점에 해당 부품 재고가 이미 있는지 확인
//            Optional<AgencyStock> optionalAgencyStock = agencyStockRepository.findByAgencyIdAndPartId(agencyId, stockInboundRequestDTO.getPartId());
//
//            if (optionalAgencyStock.isPresent()) {
//                // 이미 재고가 있으면 기존 재고 수량 증가
//                AgencyStock existingStock = optionalAgencyStock.get();
//                existingStock.increaseQuantity(stockInboundRequestDTO.getQuantity());
//            } else {
//                // 재고가 없으면 새로운 재고 생성
//                AgencyStock newStock = AgencyStock.create(agency.getId(), stockInboundRequestDTO.getPartId(), stockInboundRequestDTO.getQuantity());
//                agencyStockRepository.save(newStock);
//            }
//        }
    }

    // 재고 출고 처리 (장바구니 기능)
    @Transactional
    public void outboundStock(Long agencyId, StockOutboundRequestDTO stockOutboundRequestDTO) {

        for (StockOutboundRequestDTO.StockItem item : stockOutboundRequestDTO.getItems()) {
            mockStockDB.values().stream()
                    .filter(s -> s.getAgencyId().equals(agencyId) && s.getPartId().equals(item.getPartId()))
                    .findFirst()
                    .ifPresent(stock -> stock.decreaseQuantity(item.getQuantity()));
        }

//        for (StockOutboundRequestDTO.StockItem item : stockOutboundRequestDTO.getItems()) {
//            AgencyStock stock = agencyStockRepository.findByAgencyIdAndPartId(agencyId, item.getPartId())
//                    .orElseThrow(() -> new NotFoundException(ErrorStatus.STOCK_NOT_FOUND.getMessage()));
//
//            stock.decreaseQuantity(item.getQuantity());
//        }
    }
}
