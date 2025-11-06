package com.sampoom.backend.api.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklySummaryResponseDTO {

    private long queriedParts; // 조회된 부품 수
    private long inStockParts; // 입고된 부품 수
    private long outStockParts; // 출고된 부품 수
    private String weekPeriod; // 주간 기간 (예: "2025-11-04 ~ 2025-11-10")
}
