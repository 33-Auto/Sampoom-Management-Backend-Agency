package com.sampoom.backend.api.part.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sampoom.backend.common.mapper.PartFlatDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PartFlatResponseDTO implements PartFlatDTO {

    private Long categoryId;
    private String categoryName;
    private Long groupId;
    private String groupName;
    private Long partId;
    private String partCode;
    private String partName;
    private int stockQuantity;
    private Integer standardCost; // 표준 단가

    @QueryProjection
    public PartFlatResponseDTO(
            Long categoryId,
            String categoryName,
            Long groupId,
            String groupName,
            Long partId,
            String partCode,
            String partName,
            int stockQuantity,
            Integer standardCost
    ) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.partId = partId;
        this.partCode = partCode;
        this.partName = partName;
        this.stockQuantity = stockQuantity;
        this.standardCost = standardCost;
    }

    // 인터페이스용 getter
    @Override public Long getCategoryId() { return categoryId; }
    @Override public String getCategoryName() { return categoryName; }
    @Override public Long getGroupId() { return groupId; }
    @Override public String getGroupName() { return groupName; }
    @Override public Long getPartId() { return partId; }
    @Override public String getPartCode() { return partCode; }
    @Override public String getPartName() { return partName; }
    @Override public int getQuantity() { return stockQuantity; }
    @Override public Integer getStandardCost() { return standardCost; }
}
