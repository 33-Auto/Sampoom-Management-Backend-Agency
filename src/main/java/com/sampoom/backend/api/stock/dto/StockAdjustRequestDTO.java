package com.sampoom.backend.api.stock.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockAdjustRequestDTO {

    @NotNull(message = "수정할 재고 수량은 필수입니다.")
    @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다.") // 0개로 맞출 수도 있음
    private Long finalQuantity;

    private String reason;
}
