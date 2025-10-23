package com.sampoom.backend.api.partread.entity;

import com.sampoom.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "part")
@Getter
@NoArgsConstructor
@Immutable
public class Part extends BaseTimeEntity {

    @Id
    private Long id;

    private String code;
    private String name;
    private String status;

    @Column(name = "group_id")
    private Long groupId;
}
