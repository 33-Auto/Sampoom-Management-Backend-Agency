package com.sampoom.backend.api.part.feign.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartResponseDTO {
    private Long partId;
    private String name;
    private String code;
}
