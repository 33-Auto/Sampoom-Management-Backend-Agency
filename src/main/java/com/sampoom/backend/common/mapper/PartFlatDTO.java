package com.sampoom.backend.common.mapper;

public interface PartFlatDTO {
    default Long getCartItemId() { return null; }
    default Long getOutboundId() { return null; }
    Long getCategoryId();
    String getCategoryName();
    Long getGroupId();
    String getGroupName();
    Long getPartId();
    String getPartCode();
    String getPartName();
    int getQuantity();
}
