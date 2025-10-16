package com.sampoom.backend.api.order.repository;

import com.sampoom.backend.api.order.entity.AgencyOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgencyOrderItemRepository extends JpaRepository<AgencyOrderItem, Long> {

    // 특정 주문(orderId)에 속한 모든 품목 조회
    List<AgencyOrderItem> findByAgencyOrder_Id(Long orderId);
}