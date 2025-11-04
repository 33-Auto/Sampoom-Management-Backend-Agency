package com.sampoom.backend.api.vendor.service;

import com.sampoom.backend.api.vendor.dto.VendorPayload;
import com.sampoom.backend.api.vendor.entity.Vendor;
import com.sampoom.backend.api.vendor.entity.VendorStatus;
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

        Vendor vendor = vendorRepository.findById(payload.getVendorId())
                .orElse(Vendor.builder()
                        .id(payload.getVendorId())
                        .code(payload.getVendorCode())
                        .build());

        vendor.updateFromPayload(
                payload.getVendorName(),
                payload.getAddress(),
                payload.getLatitude(),
                payload.getLongitude(),
                VendorStatus.valueOf(payload.getStatus()),
                payload.getBusinessNumber(),
                payload.getCeoName(),
                payload.isDeleted()
        );

        vendorRepository.saveAndFlush(vendor); // DB 반영
    }

    @Transactional
    public void deleteVendor(Long vendorId) {
        vendorRepository.deleteById(vendorId);
    }
}
