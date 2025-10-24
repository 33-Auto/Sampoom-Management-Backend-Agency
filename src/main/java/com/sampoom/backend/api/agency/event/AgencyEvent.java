package com.sampoom.backend.api.agency.event;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyEvent {

    private String eventId;         // UUID (고유 이벤트 ID)
    private String eventType;       // "AgencyCreated", "AgencyUpdated", "AgencyDeleted"
    private Long version;           // Agency 엔티티의 버전 (@Version)
    private String occurredAt;      // ISO-8601 시각 (OffsetDateTime.toString())
    private Payload payload;        // 실제 데이터 (Agency 정보)

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Payload {
        private Long agencyId;
        private String name;
        private String address;
        private String status;
        private Boolean deleted;
    }
}
