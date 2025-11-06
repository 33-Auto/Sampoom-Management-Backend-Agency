package com.sampoom.backend.api.stock.repository;

import com.sampoom.backend.api.stock.entity.HistoryAction;
import com.sampoom.backend.api.stock.entity.PartHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PartHistoryRepository extends JpaRepository<PartHistory, Long> {

    // 대리점별 주간 활동 수량 합계 조회
    @Query("SELECT COALESCE(SUM(ph.quantity), 0) FROM PartHistory ph WHERE ph.agencyId = :agencyId AND ph.action = :action AND ph.date BETWEEN :startDate AND :endDate")
    Long sumQuantityByAgencyIdAndActionAndDateBetween(
            @Param("agencyId") Long agencyId,
            @Param("action") HistoryAction action,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
