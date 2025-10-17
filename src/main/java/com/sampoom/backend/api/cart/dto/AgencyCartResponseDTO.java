package com.sampoom.backend.api.cart.dto;

import com.querydsl.core.annotations.QueryProjection;
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
    private String categoryName;
    private String groupName;

    @QueryProjection
    public AgencyCartResponseDTO(Long cartItemId, Long partId, String partName, String partCode,
                                 int quantity, String groupName, String categoryName) {
        this.cartItemId = cartItemId;
        this.partId = partId;
        this.partName = partName;
        this.partCode = partCode;
        this.quantity = quantity;
        this.groupName = groupName;
        this.categoryName = categoryName;
    }
}
