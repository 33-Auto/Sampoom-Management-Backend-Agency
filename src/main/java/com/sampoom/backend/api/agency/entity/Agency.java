package com.sampoom.backend.api.agency.entity;

import com.sampoom.backend.common.entity.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "agency")
@SQLDelete(sql = "UPDATE agency SET deleted = true, status = 'INACTIVE', deleted_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
public class Agency extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AgencyStatus status; // 상태 (ACTIVE, INACTIVE 등)

    /**
     * 대리점 정보 수정 (이름, 주소, 상태)
     */
    public void updateAgency(String name, String address, AgencyStatus status) {
        if (name != null) this.name = name;
        if (address != null) this.address = address;
        if (status != null) this.status = status;
    }
}
