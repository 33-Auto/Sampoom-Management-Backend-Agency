package com.sampoom.backend.api.stock.service;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.stock.entity.AgencyStock;
import com.sampoom.backend.api.stock.repository.AgencyStockRepository;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
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
    private final AgencyRepository agencyRepository;

    // 대리점별 재고 Map (partId → quantity)
    public Map<Long, Integer> getStockByAgency(Long agencyId) {
        return agencyStockRepository.findByAgency_Id(agencyId).stream()
                .collect(Collectors.toMap(
                        stock -> stock.getPartId(),
                        stock -> stock.getQuantity()
                ));
    }

    // 재고 증가 (입고 시)
    @Transactional
    public void increaseStock(Long agencyId, Long partId, int quantity) {
        AgencyStock stock = agencyStockRepository.findByAgency_IdAndPartId(agencyId, partId)
                .orElseGet(() -> {
                    Agency agency = agencyRepository.findById(agencyId)
                            .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));
                    return AgencyStock.create(agency, partId, 0);
                });

        stock.increaseQuantity(quantity);
        agencyStockRepository.save(stock);
    }

//    // 출고 처리
//    @Transactional
//    public void outboundStock(Long agencyId, StockOutboundRequestDTO stockOutboundRequestDTO) {
//        for (StockOutboundRequestDTO.StockItem item : stockOutboundRequestDTO.getItems()) {
//            agencyStockRepository.findByAgencyIdAndPartId(agencyId, item.getPartId())
//                    .ifPresent(stock -> stock.decreaseQuantity(item.getQuantity()));
//        }
//    }
}
