package com.sampoom.backend.api.part.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartWithStockResponseDTO {
    private Long partId;
    private String name;
    private String code;
    private int quantity;  // 재고 수량
}

