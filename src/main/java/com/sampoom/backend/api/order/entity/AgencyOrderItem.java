package com.sampoom.backend.api.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "agency_order_item")
public class AgencyOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private AgencyOrder agencyOrder;

    private Long partId;

    private String partName;
    private String partCode;

    private int quantity;

    public void setOrder(AgencyOrder agencyOrder) {
        this.agencyOrder = agencyOrder;
    }

    public static AgencyOrderItem fromCartItem(Long partId, String partName, String partCode, int quantity) {
        return AgencyOrderItem.builder()
                .partId(partId)
                .partName(partName)
                .partCode(partCode)
                .quantity(quantity)
                .build();
    }
}