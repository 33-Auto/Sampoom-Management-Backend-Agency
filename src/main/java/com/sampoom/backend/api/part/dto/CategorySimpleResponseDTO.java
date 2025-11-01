package com.sampoom.backend.api.part.dto;

import com.sampoom.backend.api.part.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySimpleResponseDTO {
    private Long id;
    private String code;
    private String name;

    // Entity → DTO 변환용 static 메서드
    public static CategorySimpleResponseDTO fromEntity(Category category) {
        return CategorySimpleResponseDTO.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .build();
    }
}

