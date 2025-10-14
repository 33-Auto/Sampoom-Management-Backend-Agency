package com.sampoom.backend.api.stock.service;

import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.stock.dto.StockInboundRequestDTO;
import com.sampoom.backend.api.stock.dto.StockOutboundRequestDTO;
import com.sampoom.backend.api.stock.entity.AgencyStock;
import com.sampoom.backend.api.stock.repository.AgencyStockRepository;
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

    private final AgencyStockRepository agencyStockRepository;

    // 대리점별 재고 Map (partId → quantity)
    public Map<Long, Integer> getStockByAgency(Long agencyId) {
        return agencyStockRepository.findByAgency_Id(agencyId).stream()
                .collect(Collectors.toMap(
                        stock -> stock.getPartId(),
                        stock -> stock.getQuantity()
                ));
    }

//    // 입고 처리
//    @Transactional
//    public void inboundStock(Long agencyId, StockInboundRequestDTO stockInboundRequestDTO) {
//        for (StockInboundRequestDTO.InboundItem item : stockInboundRequestDTO.getItems()) {
//            agencyStockRepository.findByAgencyIdAndPartId(agencyId, item.getPartId())
//                    .ifPresentOrElse(
//                            stock -> stock.increaseQuantity(item.getQuantity()),
//                            () -> agencyStockRepository.save(AgencyStock.builder()
//                                    .agency(agency)
//                                    .partId(item.getPartId())
//                                    .quantity(item.getQuantity())
//                                    .build())
//                    );
//        }
//    }
//
//    // 출고 처리
//    @Transactional
//    public void outboundStock(Long agencyId, StockOutboundRequestDTO stockOutboundRequestDTO) {
//        for (StockOutboundRequestDTO.StockItem item : stockOutboundRequestDTO.getItems()) {
//            agencyStockRepository.findByAgencyIdAndPartId(agencyId, item.getPartId())
//                    .ifPresent(stock -> stock.decreaseQuantity(item.getQuantity()));
//        }
//    }
}
