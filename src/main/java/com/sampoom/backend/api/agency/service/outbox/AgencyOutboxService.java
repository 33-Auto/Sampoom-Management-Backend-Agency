package com.sampoom.backend.api.agency.service.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.agency.entity.outbox.AgencyOutbox;
import com.sampoom.backend.api.agency.entity.outbox.OutboxStatus;
import com.sampoom.backend.api.agency.event.AgencyEvent;
import com.sampoom.backend.api.agency.repository.outbox.AgencyOutboxRepository;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;


@Service
@RequiredArgsConstructor
public class AgencyOutboxService {

    private final AgencyOutboxRepository agencyOutboxRepository;
    private final ObjectMapper objectMapper;

    /**
     * Outbox 테이블에 이벤트 저장 (트랜잭션 내)
     */
    @Transactional
    public void saveEvent(AgencyEvent event) {
        try {
            AgencyOutbox outbox = AgencyOutbox.builder()
                    .aggregateType("AGENCY")
                    .aggregateId(event.getPayload().getAgencyId())
                    .eventType(event.getEventType())
                    .payload(objectMapper.writeValueAsString(event.getPayload())) // ✅ payload만 JSON 직렬화
                    .eventId(event.getEventId())
                    .version(event.getVersion())
                    .occurredAt(OffsetDateTime.parse(event.getOccurredAt()))
                    .status(OutboxStatus.READY)
                    .build();

            agencyOutboxRepository.save(outbox);
        } catch (Exception e) {
            throw new BadRequestException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
