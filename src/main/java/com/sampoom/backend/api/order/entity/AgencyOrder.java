package com.sampoom.backend.api.order.entity;

import com.sampoom.backend.common.entitiy.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agency_order")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyOrder extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long agencyId;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "agencyOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AgencyOrderItem> items = new ArrayList<>();


    public void addItem(AgencyOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void markReceived() { this.status = OrderStatus.COMPLETED; }

    public void cancel() { this.status = OrderStatus.CANCELED; }

    public static AgencyOrder create(Long agencyId, String orderNumber) {
        return AgencyOrder.builder()
                .agencyId(agencyId)
                .orderNumber(orderNumber)
                .status(OrderStatus.PENDING)
                .build();
    }
}