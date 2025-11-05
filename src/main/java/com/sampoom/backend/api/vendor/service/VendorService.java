package com.sampoom.backend.api.vendor.service;

import com.sampoom.backend.api.vendor.dto.VendorPayload;
import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.agency.entity.AgencyStatus;
import com.sampoom.backend.api.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    @Transactional
    public void createOrUpdateVendor(VendorPayload payload) {

        Agency agency = vendorRepository.findById(payload.getVendorId())
                .orElse(Agency.builder()
                        .id(payload.getVendorId())
                        .code(payload.getVendorCode())
                        .build());

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

        vendorRepository.saveAndFlush(agency); // DB 반영
    }

    @Transactional
    public void deleteVendor(Long vendorId) {
        vendorRepository.deleteById(vendorId);
    }
}
