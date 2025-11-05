package com.sampoom.backend.api.part.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.part.event.dto.PartGroupEvent;
import com.sampoom.backend.api.part.service.PartGroupEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartGroupEventConsumer {

    private final ObjectMapper objectMapper;
    private final PartGroupEventService service;

    @Transactional
    @KafkaListener(topics = "part-group-events")
    public void consume(String message) {
        try {
            PartGroupEvent event = objectMapper.readValue(message, PartGroupEvent.class);
            log.info("📦 Received PartGroup Event: {}", event.getEventType());
            service.handleGroupEvent(event);
        } catch (Exception e) {
            log.error("❌ Failed to process group event", e);
        }
    }
}
