package com.sampoom.backend.api.outbound.dto;

import com.sampoom.backend.api.outbound.entity.AgencyOutboundItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AgencyOutboundResponseDTO {

    private Long itemId;
    private Long partId;
    private String partCode;
    private String partName;
    private String groupName;
    private String categoryName;
    private int quantity;
}