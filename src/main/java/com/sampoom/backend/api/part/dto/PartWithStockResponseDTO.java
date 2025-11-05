package com.sampoom.backend.api.part.dto;

import com.sampoom.backend.api.part.entity.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartWithStockResponseDTO {
    private Long partId;
    private String code;
    private String name;
    private int quantity; // 재고 수량
    private Integer standardCost; // 표준 단가

    public static PartWithStockResponseDTO of(Part part, int quantity) {
        return PartWithStockResponseDTO.builder()
                .partId(part.getId())
                .code(part.getCode())
                .name(part.getName())
                .quantity(quantity)
                .standardCost(part.getStandardCost())
                .build();
    }
}



