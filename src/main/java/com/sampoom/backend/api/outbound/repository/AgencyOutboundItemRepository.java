package com.sampoom.backend.api.outbound.repository;

import com.sampoom.backend.api.outbound.entity.AgencyOutboundItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyOutboundItemRepository extends CrudRepository<AgencyOutboundItem, Long> {

    // 사용자별 출고목록 항목 전체 조회 (개인별)
    List<AgencyOutboundItem> findByUserId(String userId);

    // 사용자 ID + 부품 ID로 단일 항목 조회 (개인별)
    Optional<AgencyOutboundItem> findByUserIdAndPartId(String userId, Long partId);

    // 사용자별 출고목록 전체 삭제 (개인별)
    void deleteAllByUserId(String userId);

    // 기존 대리점별 메서드들도 유지 (필요시)
    List<AgencyOutboundItem> findByAgency_Id(Long agencyId);
    Optional<AgencyOutboundItem> findByAgency_IdAndPartId(Long agencyId, Long partId);
}
