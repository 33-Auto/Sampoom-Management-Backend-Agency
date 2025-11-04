package com.sampoom.backend.common.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event<T> {
    private String eventId;
    private String eventType;
    private int version;
    private String occurredAt;
    private T payload;
}
