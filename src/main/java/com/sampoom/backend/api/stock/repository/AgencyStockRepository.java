package com.sampoom.backend.api.stock.repository;

import com.sampoom.backend.api.stock.entity.AgencyStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyStockRepository extends JpaRepository<AgencyStock, Long> {

    // 특정 대리점의 재고 목록 조회
    List<AgencyStock> findByAgency_Id(Long agencyId);

    Optional<AgencyStock> findByAgency_IdAndPartId(Long agencyId, Long partId);
}
