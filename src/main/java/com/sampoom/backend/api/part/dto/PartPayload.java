package com.sampoom.backend.api.part.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartPayload {
    private Long partId;
    private String code;
    private String name;
    private String partUnit;
    private Integer baseQuantity;
    private Integer leadTime;
    private String status;
    private boolean deleted;
    private Long groupId;
    private Long categoryId;
    private Integer standardCost;
}
