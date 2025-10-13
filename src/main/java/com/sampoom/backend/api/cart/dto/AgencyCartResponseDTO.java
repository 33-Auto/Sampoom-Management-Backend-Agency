package com.sampoom.backend.api.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AgencyCartResponseDTO {

    private Long cartItemId;
    private Long partId;
    private int quantity;
    private String partCode;
    private String partName;
}
