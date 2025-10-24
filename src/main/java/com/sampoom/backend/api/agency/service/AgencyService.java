package com.sampoom.backend.api.agency.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.agency.dto.AgencyCreateRequestDTO;
import com.sampoom.backend.api.agency.dto.AgencyUpdateRequestDTO;
import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.entity.AgencyStatus;
import com.sampoom.backend.api.agency.event.AgencyEvent;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.agency.service.outbox.AgencyOutboxService;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgencyService {

    private final AgencyRepository agencyRepository;
    private final AgencyOutboxService agencyOutboxService;
    private final ObjectMapper objectMapper;

    /**
     * 대리점 생성
     */
    @Transactional
    public void createAgency(AgencyCreateRequestDTO agencyCreateRequestDTO) {
        Agency agency = Agency.builder()
                .name(agencyCreateRequestDTO.getName())
                .address(agencyCreateRequestDTO.getAddress())
                .status(agencyCreateRequestDTO.getStatus() != null ? agencyCreateRequestDTO.getStatus() : AgencyStatus.ACTIVE)
                .build();

        agencyRepository.save(agency);

        AgencyEvent event = AgencyEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("AgencyCreated")
                .version(agency.getVersion())
                .occurredAt(OffsetDateTime.now().toString())
                .payload(AgencyEvent.Payload.builder()
                        .agencyId(agency.getId())
                        .name(agency.getName())
                        .address(agency.getAddress())
                        .status(agency.getStatus().toString())
                        .deleted(false)
                        .build())
                .build();

        agencyOutboxService.saveEvent(event);
    }

    /**
     * 대리점 정보 수정 후 Outbox 이벤트 기록
     */
    @Transactional
    public void updateAgency(Long agencyId, AgencyUpdateRequestDTO agencyUpdateRequestDTO) {

        // DB에서 대리점 조회
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        // 엔티티 수정
        agency.updateAgency(agencyUpdateRequestDTO.getName(), agencyUpdateRequestDTO.getAddress(), agencyUpdateRequestDTO.getStatus());
       agencyRepository.save(agency);

        // 순수 값만 넣어서 이벤트 생성
        AgencyEvent event = AgencyEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("AgencyUpdated")
                .version(agency.getVersion())
                .occurredAt(OffsetDateTime.now().toString())
                .payload(AgencyEvent.Payload.builder()
                        .agencyId(agency.getId())
                        .name(agency.getName())
                        .address(agency.getAddress())
                        .status(agency.getStatus().toString())
                        .deleted(false)
                        .build())
                .build();

        // 반드시 이벤트 객체를 전달해야 함
        agencyOutboxService.saveEvent(event);
    }

    /**
     * 대리점 삭제
     */
    @Transactional
    public void deleteAgency(Long agencyId) {
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.AGENCY_NOT_FOUND));

        agency.updateAgency(agency.getName(), agency.getAddress(), AgencyStatus.INACTIVE);
        agencyRepository.saveAndFlush(agency);

        // 삭제 이벤트 발행
        AgencyEvent event = AgencyEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("AgencyDeleted")
                .version(agency.getVersion())
                .occurredAt(OffsetDateTime.now().toString())
                .payload(AgencyEvent.Payload.builder()
                        .agencyId(agency.getId())
                        .name(agency.getName())
                        .address(agency.getAddress())
                        .status(agency.getStatus().toString())
                        .deleted(true)
                        .build())
                .build();

        agencyOutboxService.saveEvent(event);

        // 실제 DB 삭제는 논리 삭제면 생략 가능
        agencyRepository.delete(agency);
    }
}
