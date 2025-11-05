package com.sampoom.backend.api.part.event.dto;


import com.sampoom.backend.api.part.dto.PartPayload;
import lombok.*;

@Getter
@Setter
@ToString
public class PartEvent {
    private String eventId;
    private String eventType;
    private Long version;
    private String occurredAt;
    private PartPayload payload;
}