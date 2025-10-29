package com.sampoom.backend.api.part.projection;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartCategoryProjection implements ProjectionMetadata {

    @Id
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    // ⭐️ 이벤트 추적 필드
    @Column(nullable = false)
    private Long version;

    @Column(name = "last_event_id")
    private UUID lastEventId;

    @Column(nullable = false)
    private Boolean deleted = false;

    private OffsetDateTime sourceUpdatedAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

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