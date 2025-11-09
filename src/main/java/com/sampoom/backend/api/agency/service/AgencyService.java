package com.sampoom.backend.api.agency.service;

import com.sampoom.backend.api.vendor.dto.VendorPayload;
import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.entity.AgencyStatus;
import com.sampoom.backend.api.agency.repository.AgencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgencyService {

    private final AgencyRepository agencyRepository;

    @Transactional
    public void createOrUpdateAgencyFromVendorEvent(VendorPayload payload) {
        log.info("🏢 Processing agency from vendor event - ID: {}, Code: {}, Name: {}",
                payload.getVendorId(), payload.getVendorCode(), payload.getVendorName());

        Agency agency = agencyRepository.findById(payload.getVendorId())
                .orElseGet(() -> agencyRepository.findByCode(payload.getVendorCode())
                        .orElse(Agency.builder()
                                .id(payload.getVendorId())
                                .code(payload.getVendorCode())
                                .build()));


        log.info("📋 Before update - Agency code: {}, name: {}", agency.getCode(), agency.getName());

        agency.updateFromVendorEvent(
                payload.getVendorCode(),
                payload.getVendorName(),
                payload.getAddress(),
                payload.getLatitude(),
                payload.getLongitude(),
                AgencyStatus.valueOf(payload.getStatus()),
                payload.getBusinessNumber(),
                payload.getCeoName(),
                payload.isDeleted()
        );

        Agency saved = agencyRepository.saveAndFlush(agency);
        log.info("💾 After save - Agency ID: {}, code: {}, name: {}, businessNumber: {}, ceoName: {}",
                saved.getId(), saved.getCode(), saved.getName(), saved.getBusinessNumber(), saved.getCeoName());
    }

    @Transactional
    public void deleteAgency(Long agencyId) {
        agencyRepository.deleteById(agencyId);
    }
}
