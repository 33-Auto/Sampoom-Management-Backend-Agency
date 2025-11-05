package com.sampoom.backend.api.part.entity;

import com.sampoom.backend.api.part.dto.PartCategoryPayload;
import com.sampoom.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseTimeEntity {

    @Id
    private Long id;

    private String code;
    private String name;

    // ✅ 이벤트 기반 갱신용 메서드
    public void updateFromPayload(PartCategoryPayload payload) {
        this.name = payload.getCategoryName();
        this.code = payload.getCategoryCode();
    }
}