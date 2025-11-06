package com.sampoom.backend.api.stock.repository;

import com.sampoom.backend.api.stock.entity.PartHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PartHistoryRepository extends JpaRepository<PartHistory, Long> {

    // 주어진 기간 동안 활동 유형에 맞는 활동을 조회
    List<PartHistory> findByActionAndDateBetween(String action, LocalDate startDate, LocalDate endDate);

    // 대리점별 주어진 기간 동안 활동 유형에 맞는 활동을 조회
    List<PartHistory> findByAgencyIdAndActionAndDateBetween(Long agencyId, String action, LocalDateTime startDate, LocalDateTime endDate);

    // 대리점별 주간 활동 통계 조회 (건수)
    long countByAgencyIdAndActionAndDateBetween(Long agencyId, String action, LocalDateTime startDate, LocalDateTime endDate);

    // 대리점별 주간 활동 수량 합계 조회
    @Query("SELECT COALESCE(SUM(ph.quantity), 0) FROM PartHistory ph WHERE ph.agencyId = :agencyId AND ph.action = :action AND ph.date BETWEEN :startDate AND :endDate")
    Long sumQuantityByAgencyIdAndActionAndDateBetween(@Param("agencyId") Long agencyId, @Param("action") String action, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
