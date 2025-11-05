package com.sampoom.backend.api.part.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.part.dto.PartPayload;
import com.sampoom.backend.api.part.entity.Category;
import com.sampoom.backend.api.part.entity.Part;
import com.sampoom.backend.api.part.entity.PartGroup;
import com.sampoom.backend.api.part.event.dto.PartEvent;
import com.sampoom.backend.api.part.repository.CategoryRepository;
import com.sampoom.backend.api.part.repository.PartGroupRepository;
import com.sampoom.backend.api.part.repository.PartRepository;
import com.sampoom.backend.api.part.service.PartEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartEventConsumer {

    private final ObjectMapper objectMapper;
    private final PartEventService partEventService;

    @Transactional
    @KafkaListener(topics = "part-events")
    public void consume(String message) {
        try {
            // Kafka 메시지 수신 후 로그
            log.info("📦 Received raw message: {}", message);

            // 메시지 -> PartEvent 변환 후 로그
            PartEvent event = objectMapper.readValue(message, PartEvent.class);
            log.info("📦 Received Part Event: {}", event.getEventType());

            // event의 Payload 로그
            log.debug("📦 Event Payload: {}", event.getPayload());

            // PartEvent 처리
            partEventService.handlePartEvent(event);

        } catch (Exception e) {
            log.error("❌ Failed to process part event", e);
        }
    }
}
