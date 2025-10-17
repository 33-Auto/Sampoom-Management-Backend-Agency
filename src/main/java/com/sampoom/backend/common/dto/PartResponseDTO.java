package com.sampoom.backend.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartResponseDTO {
    private Long cartItemId;   // 장바구니일 때만 존재
    private Long outboundId;   // 출고일 때만 존재
    private Long partId;
    private String code;
    private String name;
    private int quantity;
}
