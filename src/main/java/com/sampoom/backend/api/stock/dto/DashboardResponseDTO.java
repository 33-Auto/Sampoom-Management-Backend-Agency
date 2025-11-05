package com.sampoom.backend.api.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {

    private long totalParts; // 보유 부품 수
    private long outOfStockParts; // 품절 부품 수 (재고 0개)
    private long lowStockParts; // 부족 부품 수 (재고 10개 이하)
    private long totalQuantity; // 총 재고 수량
}
