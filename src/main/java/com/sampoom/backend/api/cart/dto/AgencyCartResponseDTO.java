package com.sampoom.backend.api.cart.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sampoom.backend.common.mapper.PartFlatDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AgencyCartResponseDTO implements PartFlatDTO {
    private Long cartItemId;
    private Long partId;
    private String partName;
    private String partCode;
    private int quantity;
    private Long groupId;
    private String groupName;
    private Long categoryId;
    private String categoryName;

    @QueryProjection
    public AgencyCartResponseDTO(
            Long cartItemId,
            Long partId,
            String partName,
            String partCode,
            int quantity,
            Long groupId,
            String groupName,
            Long categoryId,
            String categoryName
    ) {
        this.cartItemId = cartItemId;
        this.partId = partId;
        this.partCode = partCode;
        this.partName = partName;
        this.quantity = quantity;
        this.groupId = groupId;
        this.groupName = groupName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // PartFlatDTO getter 구현 (Mapper용)
    @Override public Long getCategoryId() { return categoryId; }
    @Override public String getCategoryName() { return categoryName; }
    @Override public Long getGroupId() { return groupId; }
    @Override public String getGroupName() { return groupName; }
    @Override public Long getPartId() { return partId; }
    @Override public String getPartCode() { return partCode; }
    @Override public String getPartName() { return partName; }
    @Override public int getQuantity() { return quantity; }
}
