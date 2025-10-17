package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.common.dto.CategoryResponseDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AgencyOrderResponseDTO {

    private Long orderId;          // 주문 ID
    private String orderNumber;    // 주문번호
    private LocalDateTime createdAt; // 주문일자
    private OrderStatus status;    // 주문상태

    private String agencyName;     // 대리점명

    private List<CategoryResponseDTO> items;
}
