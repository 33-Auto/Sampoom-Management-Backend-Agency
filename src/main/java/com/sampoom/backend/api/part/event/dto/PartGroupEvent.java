package com.sampoom.backend.api.part.event.dto;

import com.sampoom.backend.api.part.dto.PartGroupPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartGroupEvent {
    private String eventId;
    private String eventType;
    private Long version;
    private String occurredAt;
    private PartGroupPayload payload;
}
