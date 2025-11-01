package com.sampoom.backend.api.part.consumer;

import com.sampoom.backend.api.part.event.dto.PartCategoryEvent;
import com.sampoom.backend.api.part.event.dto.PartEvent;
import com.sampoom.backend.api.part.event.dto.PartGroupEvent;
import com.sampoom.backend.api.part.service.ProjectionUpdateService;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final ProjectionUpdateService projectionUpdateService;

    @KafkaListener(
            topics = "part-events",
            groupId = "${spring.kafka.consumer.group-id}", // YML 설정 활용
            containerFactory = "kafkaListenerContainerFactory", // KafkaConfig에서 정의된 팩토리
            // ⭐️ [필수] JSON을 PartEvent 객체로 변환하도록 명시
            properties = {"spring.json.value.default.type=com.sampoom.backend.api.part.event.dto.PartEvent"}
    )
    // @Transactional은 Service 계층에 위임 (ProjectionUpdateService에 이미 붙어있음)
    public void listenPartEvents(@Payload PartEvent event) {
        try {
            projectionUpdateService.applyPartEvent(event);
        } catch (Exception e) {
            log.error("❌ PartEvent 처리 실패: eventId={}", event.getEventId(), e);
            // ⭐️ 처리 실패 시 Kafka가 메시지를 재시도하도록 RuntimeException 던짐
            throw new RuntimeException(ErrorStatus.INTERNAL_SERVER_ERROR.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = "part-group-events",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=com.sampoom.backend.api.part.event.dto.PartGroupEvent"}
    )
    public void listenPartGroupEvents(@Payload PartGroupEvent event) {
        try {
            projectionUpdateService.applyPartGroupEvent(event);
        } catch (Exception e) {
            log.error("❌ PartGroupEvent 처리 실패: eventId={}", event.getEventId(), e);
            throw new RuntimeException(ErrorStatus.INTERNAL_SERVER_ERROR.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = "part-category-events",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory",
            properties = {"spring.json.value.default.type=com.sampoom.backend.api.part.event.dto.PartCategoryEvent"}
    )
    public void listenCategoryEvents(@Payload PartCategoryEvent event) {
        try {
            projectionUpdateService.applyCategoryEvent(event);
        } catch (Exception e) {
            log.error("❌ CategoryEvent 처리 실패: eventId={}", event.getEventId(), e);
            throw new RuntimeException(ErrorStatus.INTERNAL_SERVER_ERROR.getMessage(), e);
        }
    }
}
