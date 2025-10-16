package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.AgencyOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyOrderItemRepository extends JpaRepository<AgencyOrderItem, Long> {
}