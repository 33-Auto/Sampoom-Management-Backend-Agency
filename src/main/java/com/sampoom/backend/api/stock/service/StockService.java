package com.sampoom.backend.api.stock.service;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.stock.dto.DashboardResponseDTO;
import com.sampoom.backend.api.stock.dto.PartUpdateRequestDTO;
import com.sampoom.backend.api.stock.dto.WeeklySummaryResponseDTO;
import com.sampoom.backend.api.stock.entity.AgencyStock;
import com.sampoom.backend.api.stock.entity.HistoryAction;
import com.sampoom.backend.api.stock.entity.PartHistory;
import com.sampoom.backend.api.stock.repository.AgencyStockRepository;
import com.sampoom.backend.api.stock.repository.DashboardQueryRepository;
import com.sampoom.backend.api.stock.repository.PartHistoryRepository;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final AgencyStockRepository agencyStockRepository;
    private final DashboardQueryRepository dashboardQueryRepository;
    private final AgencyRepository agencyRepository;
    private final PartHistoryRepository partHistoryRepository;

    // 대리점별 재고 Map (partId → quantity)
    public Map<Long, Integer> getStockByAgency(Long agencyId) {
        return agencyStockRepository.findByAgency_Id(agencyId).stream()
                .collect(Collectors.toMap(
                        stock -> stock.getPartId(),
                        stock -> stock.getQuantity()
                ));
    }

    // 입고 처리
    @Transactional
    public void processOrderReceiving(Long agencyId, PartUpdateRequestDTO partUpdateRequestDTO) {

        // 대리점 존재 여부 확인
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        if (partUpdateRequestDTO.getItems() == null || partUpdateRequestDTO.getItems().isEmpty()) {
            return;
        }

        // 각 입고 품목에 대해 재고를 처리
        partUpdateRequestDTO.getItems().forEach(item -> {

            Long partId = item.getPartId();
            int quantityToAdd = item.getQuantity();

            // 기존 AgencyStock 레코드를 찾거나, 없으면 새로 생성 후 저장합니다.
            agencyStockRepository.findByAgency_IdAndPartId(agencyId, partId)
                    .ifPresentOrElse(
                            // 재고가 이미 있는 경우 수량 증가 후 저장
                            stock -> {
                                stock.increaseQuantity(quantityToAdd);
                                agencyStockRepository.save(stock);

                                // 입고 히스토리 저장 (수량 포함)
                                PartHistory inboundHistory = PartHistory.createInboundHistory(agencyId, partId, quantityToAdd);
                                partHistoryRepository.save(inboundHistory);
                            },
                            // 재고가 없는 경우 새 AgencyStock 레코드 생성 후 저장
                            () -> {
                                AgencyStock newStock = AgencyStock.create(agency, partId, quantityToAdd);
                                agencyStockRepository.save(newStock);

                                // 입고 히스토리 저장 (수량 포함)
                                PartHistory inboundHistory = PartHistory.createInboundHistory(agencyId, partId, quantityToAdd);
                                partHistoryRepository.save(inboundHistory);
                            }
                    );
        });
    }

    // 재고 감소 (출고 처리)
    @Transactional
    public void decreaseStock(Long agencyId, Long partId, int quantity) {
        AgencyStock stock = agencyStockRepository.findByAgency_IdAndPartId(agencyId, partId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PART_NOT_FOUND));

        stock.decreaseQuantity(quantity);
        agencyStockRepository.save(stock);

        // 출고 히스토리 저장 (수량 포함)
        PartHistory outboundHistory = PartHistory.createOutboundHistory(agencyId, partId, quantity);
        partHistoryRepository.save(outboundHistory);
    }

    // 대시보드용 재고 요약 정보 (모든 부품 기준)
    public DashboardResponseDTO getDashboardData(Long agencyId) {
        // 대리점 존재 여부 확인
        agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        // QueryRepository를 통해 모든 부품 기준으로 계산
        DashboardResponseDTO result = dashboardQueryRepository.getDashboardData(agencyId);

        return result;
    }

    // 주간 요약 데이터 반환 (대리점별 - 실제 히스토리 기반)
    public WeeklySummaryResponseDTO getWeeklySummaryData(Long agencyId) {
        // 대리점 존재 여부 확인
        agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        // 이번 주 기간 계산 (월요일 00:00 ~ 일요일 23:59)
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(23, 59, 59);

        String weekPeriod = String.format("%s ~ %s", startOfWeek, endOfWeek);

        // 실제 PartHistory 데이터 기반으로 수량 합계 계산 (조회 제외)
        Long inStockPartsResult = partHistoryRepository.sumQuantityByAgencyIdAndActionAndDateBetween(
                agencyId, HistoryAction.INBOUND, startDateTime, endDateTime);
        long inStockParts = inStockPartsResult != null ? inStockPartsResult : 0L;

        Long outStockPartsResult = partHistoryRepository.sumQuantityByAgencyIdAndActionAndDateBetween(
                agencyId, HistoryAction.OUTBOUND, startDateTime, endDateTime);
        long outStockParts = outStockPartsResult != null ? outStockPartsResult : 0L;

        WeeklySummaryResponseDTO result = WeeklySummaryResponseDTO.builder()
                .inStockParts(inStockParts)
                .outStockParts(outStockParts)
                .weekPeriod(weekPeriod)
                .build();

        return result;
    }
}

