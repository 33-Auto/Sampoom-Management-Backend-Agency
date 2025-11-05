package com.sampoom.backend.api.part.entity;

import com.sampoom.backend.api.part.dto.PartGroupPayload;
import com.sampoom.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "part_group")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartGroup extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    public void updateFromPayload(PartGroupPayload payload) {
        this.name = payload.getGroupName();
        this.code = payload.getGroupCode();
        this.categoryId = payload.getCategoryId();
    }
}
