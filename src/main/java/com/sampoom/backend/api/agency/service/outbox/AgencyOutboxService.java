package com.sampoom.backend.api.agency.service.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.agency.entity.outbox.AgencyOutbox;
import com.sampoom.backend.api.agency.entity.outbox.OutboxStatus;
import com.sampoom.backend.api.agency.event.AgencyUpdatedEvent;
import com.sampoom.backend.api.agency.repository.outbox.AgencyOutboxRepository;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AgencyOutboxService {

    private final AgencyOutboxRepository agencyOutboxRepository;
    private final ObjectMapper objectMapper;

    /**
     * Outbox 테이블에 이벤트 저장 (트랜잭션 내)
     */
    @Transactional
    public void saveEvent(AgencyUpdatedEvent agencyUpdatedEvent) {
        try {

            String payload = objectMapper.writeValueAsString(agencyUpdatedEvent);

            AgencyOutbox outbox = AgencyOutbox.builder()
                    .aggregateType("Agency")
                    .aggregateId(agencyUpdatedEvent.getAgencyId())
                    .eventType(agencyUpdatedEvent.getClass().getSimpleName())
                    .payload(payload)
                    .status(OutboxStatus.READY)
                    .retryCount(0)
                    .build();

            agencyOutboxRepository.save(outbox);

        } catch (JsonProcessingException e) {
            throw new BadRequestException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
