package com.sampoom.backend.api.agency.entity;

import com.sampoom.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agency")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agency extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(unique = true, nullable = false)
    private String name;         // 대리점명

    @Column(unique = true, nullable = false)
    private String address;      // 주소

    private Double latitude;
    private Double longitude;

    private String businessNumber;
    private String ceoName;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AgencyStatus status = AgencyStatus.ACTIVE;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean deleted = false;

    /** vendor 이벤트로부터 agency 정보 업데이트 */
    public void updateFromVendorEvent(
            String code,
            String name,
            String address,
            Double latitude,
            Double longitude,
            AgencyStatus status,
            String businessNumber,
            String ceoName,
            boolean deleted
    ) {
        this.code = code;
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
