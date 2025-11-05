package com.sampoom.backend.api.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartDeltaDTO {

    @NotNull(message = "부품 ID는 필수입니다.")
    private Long partId;

    @Min(value = 1, message = "입고 수량은 1 이상이어야 합니다.")
    private Integer quantity;
}
