package com.sampoom.backend.api.partread.dto;

import com.sampoom.backend.api.partread.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String code;
    private String name;

    // Entity → DTO 변환용 static 메서드
    public static CategoryResponseDTO fromEntity(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .build();
    }
}

