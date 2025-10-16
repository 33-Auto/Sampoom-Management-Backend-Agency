package com.sampoom.backend.api.outbound.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AgencyOutboundRequestDTO {

    @NotNull(message = "부품 ID는 필수입니다.")
    private Long partId;

    @NotNull(message = "출고 수량은 필수입니다.")
    @Positive(message = "출고 수량은 0보다 커야 합니다.")
    private int quantity;
}