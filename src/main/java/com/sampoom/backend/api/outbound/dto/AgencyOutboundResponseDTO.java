package com.sampoom.backend.api.outbound.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sampoom.backend.common.mapper.PartFlatDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AgencyOutboundResponseDTO implements PartFlatDTO {
    private Long outboundId;
    private Long partId;
    private String partName;
    private String partCode;
    private int quantity;
    private Long groupId;
    private String groupName;
    private Long categoryId;
    private String categoryName;
    private Integer standardCost; // 표준 단가

    @QueryProjection
    public AgencyOutboundResponseDTO(
            Long outboundId,
            Long partId,
            String partName,
            String partCode,
            int quantity,
            Long groupId,
            String groupName,
            Long categoryId,
            String categoryName,
            Integer standardCost
    ) {
        this.outboundId = outboundId;
        this.partId = partId;
        this.partCode = partCode;
        this.partName = partName;
        this.quantity = quantity;
        this.groupId = groupId;
        this.groupName = groupName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.standardCost = standardCost;
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
    @Override public Integer getStandardCost() { return standardCost; } // 실제 가격 정보 반환
}
