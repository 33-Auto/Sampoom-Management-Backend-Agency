package com.sampoom.backend.api.part.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PartCategoryPayload {
    private Long categoryId;
    private String categoryName;
    private String categoryCode;
}

