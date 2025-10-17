package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.AgencyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgencyOrderRepository extends JpaRepository<AgencyOrder, Long> {

    List<AgencyOrder> findByAgencyId(Long agencyId);

    List<AgencyOrder> findByAgencyIdOrderByCreatedAtDesc(Long agencyId);

    Optional<AgencyOrder> findByIdAndAgencyId(Long orderId, Long agencyId);

    @Query("SELECT COUNT(o) FROM AgencyOrder o WHERE o.createdAt BETWEEN :start AND :end")
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}