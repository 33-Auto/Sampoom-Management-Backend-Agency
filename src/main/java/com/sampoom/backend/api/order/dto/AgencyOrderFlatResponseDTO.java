package com.sampoom.backend.api.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sampoom.backend.api.order.entity.OrderStatus;
import com.sampoom.backend.common.mapper.PartFlatDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AgencyOrderFlatResponseDTO implements PartFlatDTO {
    private Long orderId;
    private String orderNumber;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String agencyName;

    private Long categoryId;
    private String categoryName;
    private Long groupId;
    private String groupName;
    private Long partId;
    private String partCode;
    private String partName;
    private int quantity;

    @QueryProjection
    public AgencyOrderFlatResponseDTO(
            Long orderId,
            String orderNumber,
            LocalDateTime createdAt,
            OrderStatus status,
            String agencyName,
            Long categoryId,
            String categoryName,
            Long groupId,
            String groupName,
            Long partId,
            String partCode,
            String partName,
            int quantity
    ) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.createdAt = createdAt;
        this.status = status;
        this.agencyName = agencyName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.partId = partId;
        this.partCode = partCode;
        this.partName = partName;
        this.quantity = quantity;
    }

    // 인터페이스용 getter
    @Override public Long getCategoryId() { return categoryId; }
    @Override public String getCategoryName() { return categoryName; }
    @Override public Long getGroupId() { return groupId; }
    @Override public String getGroupName() { return groupName; }
    @Override public Long getPartId() { return partId; }
    @Override public String getPartCode() { return partCode; }
    @Override public String getPartName() { return partName; }
    @Override public int getQuantity() { return quantity; }
}