package com.sampoom.backend.api.order.dto;

import com.sampoom.backend.api.order.entity.AgencyOrder;
import com.sampoom.backend.api.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class AgencyOrderResponseDTO {

    private Long orderId;  // 주문 id
    private Long agencyId;
    private String orderNumber;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<AgencyOrderItemResponseDTO> items;

    public static AgencyOrderResponseDTO fromEntity(AgencyOrder order) {
        return AgencyOrderResponseDTO.builder()
                .orderId(order.getId())
                .agencyId(order.getAgencyId())
                .orderNumber(order.getOrderNumber())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .items(order.getItems().stream()
                        .map(AgencyOrderItemResponseDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
