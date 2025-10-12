package com.sampoom.backend.api.stock.repository;

import com.sampoom.backend.api.stock.entity.AgencyStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyStockRepository extends JpaRepository<AgencyStock, Long> {

    // 특정 대리점의 재고 목록 조회
    List<AgencyStock> findByAgencyId(Long agencyId);

    // 특정 대리점의 partId로 재고를 찾음
    Optional<AgencyStock> findByAgencyIdAndPartId(Long agencyId, Long partId);
}
