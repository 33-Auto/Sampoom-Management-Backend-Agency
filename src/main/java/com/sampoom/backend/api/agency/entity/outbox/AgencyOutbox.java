package com.sampoom.backend.api.agency.entity.outbox;

import com.sampoom.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "agency_outbox")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgencyOutbox extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;   // 예: "Agency"
    private Long aggregateId;       // 예: agencyId
    private String eventType;       // 예: "AgencyUpdatedEvent"

    @Column(columnDefinition = "TEXT")
    private String payload;         // JSON 데이터

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;    // READY / PUBLISHED / FAILED

    private int retryCount;

    private LocalDateTime publishedAt;

    @Column(nullable = false, updatable = false, unique = true)
    private String eventId;         // 이벤트 고유 ID (UUID)

    @Column(nullable = false)
    private Long version;           // Agency 엔티티의 버전

    @Column(nullable = false)
    private OffsetDateTime occurredAt; // 이벤트 발생 시간 (ISO-8601)


    // 상태 변경용 유틸
    public void markPublished() {
        this.status = OutboxStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = OutboxStatus.FAILED;
        this.retryCount += 1;
    }
}
