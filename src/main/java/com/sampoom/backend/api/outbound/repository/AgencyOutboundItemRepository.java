package com.sampoom.backend.api.outbound.repository;

import com.sampoom.backend.api.outbound.entity.AgencyOutboundItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyOutboundItemRepository extends JpaRepository<AgencyOutboundItem, Long> {

    // 대리점별 출고 목록 조회
    List<AgencyOutboundItem> findByAgency_Id(Long agencyId);

    // 대리점별, 부품별 단일 항목 조회 (이미 담긴 부품인지 확인용)
    Optional<AgencyOutboundItem> findByAgency_IdAndPartId(Long agencyId, Long partId);
}