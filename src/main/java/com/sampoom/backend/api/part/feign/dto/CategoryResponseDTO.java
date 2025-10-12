package com.sampoom.backend.api.part.feign.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponseDTO {
    private Long categoryId;
    private String name;
}
