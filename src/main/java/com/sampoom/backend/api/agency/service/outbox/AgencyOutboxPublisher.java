package com.sampoom.backend.api.agency.service.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.agency.entity.outbox.AgencyOutbox;
import com.sampoom.backend.api.agency.entity.outbox.OutboxStatus;
import com.sampoom.backend.api.agency.event.AgencyUpdatedEvent;
import com.sampoom.backend.api.agency.repository.outbox.AgencyOutboxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgencyOutboxPublisher {

    private final AgencyOutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * READY 상태 이벤트를 Kafka로 발행
     */
    @Scheduled(fixedDelay = 5000) // 5초마다 실행
    public void publishReadyEvents() {
        List<AgencyOutbox> events =
                outboxRepository.findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus.READY);

        for (AgencyOutbox outbox : events) {
            publishSingleEvent(outbox);
        }
    }

    /**
     * 개별 이벤트 발행 (트랜잭션 보장)
     */
    @Transactional
    public void publishSingleEvent(AgencyOutbox outbox) {
        try {
            // JSON 문자열(String) → 객체로 역직렬화
            AgencyUpdatedEvent event = objectMapper.readValue(outbox.getPayload(), AgencyUpdatedEvent.class);

            kafkaTemplate.send("agency-events", event).get();

            outbox.markPublished();
            outboxRepository.save(outbox);
            log.info("Kafka 발행 성공: id={}, type={}", outbox.getId(), outbox.getEventType());
        } catch (Exception e) {
            outbox.markFailed();
            outboxRepository.save(outbox);
            log.error("Kafka 발행 실패: {}", e.getMessage());
        }
    }
}
