package com.sampoom.backend.api.part.event.dto;

import com.sampoom.backend.api.part.dto.PartCategoryPayload;
import lombok.*;

@Getter
@Setter
@ToString
public class PartCategoryEvent {
    private String eventId;
    private String eventType;
    private Long version;
    private String occurredAt;
    private PartCategoryPayload payload;
}