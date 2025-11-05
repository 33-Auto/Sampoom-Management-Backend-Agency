package com.sampoom.backend.api.vendor.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VendorPayload {
    private Long vendorId;
    private String vendorCode;
    private String vendorName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String businessNumber;
    private String ceoName;
    private String status;
    private boolean deleted;
}