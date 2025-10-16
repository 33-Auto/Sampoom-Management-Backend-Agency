package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.AgencyOrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AgencyOrderItemResponseDTO {

    private Long partId;      // 부품 ID
    private String partName;  // 부품명
    private String partCode;  // 부품 코드
    private int quantity;     // 주문 수량
    private String categoryName;
    private String groupName;
}