package com.sampoom.backend.api.agency.service;

import com.sampoom.backend.api.agency.dto.AgencyCreateRequestDTO;
import com.sampoom.backend.api.agency.dto.AgencyUpdateRequestDTO;
import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.entity.AgencyStatus;
import com.sampoom.backend.api.agency.event.AgencyUpdatedEvent;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import com.sampoom.backend.api.agency.service.outbox.AgencyOutboxService;
import com.sampoom.backend.common.exception.NotFoundException;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgencyService {

    private final AgencyRepository agencyRepository;
    private final AgencyOutboxService agencyOutboxService;

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

        AgencyUpdatedEvent event = new AgencyUpdatedEvent(
                agency.getId(),
                agency.getName(),
                agency.getAddress(),
                agency.getStatus().name(),
                false
        );

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
        AgencyUpdatedEvent event = new AgencyUpdatedEvent(
                agency.getId(),
                agency.getName(),
                agency.getAddress(),
                agency.getStatus().name(),
                false
        );

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

        // 상태를 INACTIVE로 변경
        agency.updateAgency(null, null, AgencyStatus.INACTIVE);
        agencyRepository.save(agency); // 먼저 상태 업데이트

        agencyRepository.delete(agency);

        AgencyUpdatedEvent event = new AgencyUpdatedEvent(
                agency.getId(),
                agency.getName(),
                agency.getAddress(),
                agency.getStatus().name(),
                true
        );

        agencyOutboxService.saveEvent(event);
    }
}
