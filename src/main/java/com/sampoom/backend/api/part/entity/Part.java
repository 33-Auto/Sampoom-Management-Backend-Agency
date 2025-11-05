package com.sampoom.backend.api.part.entity;

import com.sampoom.backend.api.part.dto.PartPayload;
import com.sampoom.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "part")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Part extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String partUnit;

    private Integer baseQuantity;

    private Integer leadTime;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PartStatus status = PartStatus.ACTIVE;

    private boolean deleted;

    private Integer standardCost;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    public void updateFromPayload(PartPayload payload) {
        this.code = payload.getCode();
        this.name = payload.getName();
        this.partUnit = payload.getPartUnit();
        this.baseQuantity = payload.getBaseQuantity();
        this.leadTime = payload.getLeadTime();
        this.status = PartStatus.valueOf(payload.getStatus());
        this.deleted = payload.isDeleted();
        this.standardCost = payload.getStandardCost();
        this.groupId = payload.getGroupId();
        this.categoryId = payload.getCategoryId();
    }
}
