package com.sampoom.backend.api.stock.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StockOutboundRequestDTO {

    private List<StockItem> items;

    @Getter
    @NoArgsConstructor
    public static class StockItem {

        @NotNull(message = "부품 ID는 필수입니다.")
        private Long partId;

        @NotNull(message = "출고 수량은 필수입니다.")
        @Positive(message = "출고 수량은 0보다 커야 합니다.")
        private int quantity;
    }
}
