package com.sampoom.backend.api.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StockResponseDTO {

    private Long stockId;
    private Long agencyId;
    private Long partId;
    private int quantity;
    private String partName;
    private String partCode;
}
