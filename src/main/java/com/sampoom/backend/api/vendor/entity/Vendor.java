package com.sampoom.backend.api.vendor.entity;

import com.sampoom.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vendor")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendor extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(unique = true, nullable = false)
    private String name;         // 지점명

    @Column(unique = true, nullable = false)
    private String address;      // 주소

    private Double latitude;
    private Double longitude;

    private String businessNumber;
    private String ceoName;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private VendorStatus status = VendorStatus.ACTIVE;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean deleted = false;

    /** 이벤트 기반 업데이트용 헬퍼 메서드 */
    public void updateFromPayload(
            String name,
            String address,
            Double latitude,
            Double longitude,
            VendorStatus status,
            String businessNumber,
            String ceoName,
            boolean deleted
    ) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.businessNumber = businessNumber;
        this.ceoName = ceoName;
        this.deleted = deleted;
    }
}
