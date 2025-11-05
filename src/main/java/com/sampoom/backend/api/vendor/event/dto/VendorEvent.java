package com.sampoom.backend.api.vendor.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HR 서비스에서 발행한 Vendor 이벤트 구조
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorEvent {
    private Long vendorId;
    private String vendorCode;
    private String vendorName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String businessNumber;
    private String ceoName;
    private String status; // ACTIVE / INACTIVE
    private boolean deleted;
}
