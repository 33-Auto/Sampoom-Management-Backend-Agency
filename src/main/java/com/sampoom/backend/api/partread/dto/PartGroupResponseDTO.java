package com.sampoom.backend.api.partread.dto;

import com.sampoom.backend.api.partread.entity.PartGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartGroupResponseDTO {
    private Long id;
    private String code;
    private String name;
    private Long categoryId;

    public static PartGroupResponseDTO fromEntity(PartGroup group) {
        return PartGroupResponseDTO.builder()
                .id(group.getId())
                .code(group.getCode())
                .name(group.getName())
                .categoryId(group.getCategoryId())
                .build();
    }
}
