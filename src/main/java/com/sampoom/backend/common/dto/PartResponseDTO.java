package com.sampoom.backend.common.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartResponseDTO {
    private Long partId;
    private String code;
    private String name;
    private int quantity;
}
