package com.sampoom.backend.api.part.projection;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "part")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartProjection implements ProjectionMetadata {

    @Id
    private Long id; // Master의 PK

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    // 이벤트 추적 및 멱등성 필드
    @Column(nullable = false)
    private Long version; // 이벤트 버전 체크용

    @Column(name = "last_event_id")
    private UUID lastEventId; // 멱등성 보조 체크용

    @Column(nullable = false)
    private Boolean deleted = false;

    private OffsetDateTime sourceUpdatedAt; // Master DB의 업데이트 시각

    @Column(nullable = false)
    private OffsetDateTime updatedAt; // 로컬 DB 반영 시각

    @PrePersist
    void onCreate() {
        if (updatedAt == null) updatedAt = OffsetDateTime.now();
        if (deleted == null) deleted = false;
        if (version == null) version = 0L;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}