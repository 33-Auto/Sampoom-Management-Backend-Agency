package com.sampoom.backend.api.part.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.part.event.dto.PartCategoryEvent;
import com.sampoom.backend.api.part.service.PartCategoryEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartCategoryEventConsumer {

    private final ObjectMapper objectMapper;
    private final PartCategoryEventService service;

    @Transactional
    @KafkaListener(topics = "part-category-events")
    public void consume(String message) {
        try {
            PartCategoryEvent event = objectMapper.readValue(message, PartCategoryEvent.class);
            log.info("📦 Received Category Event: {}", event.getEventType());
            service.handleCategoryEvent(event);
        } catch (Exception e) {
            log.error("❌ Failed to process category event", e);
        }
    }
}
