package com.sampoom.backend.api.order.entity;

import com.sampoom.backend.common.entitiy.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(
            name = "order_seq",
            sequenceName = "agency_order_seq",
            allocationSize = 1
    )
    private Long id;

    private Long agencyId;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "agencyOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AgencyOrderItem> items = new ArrayList<>();

    @PostPersist
    public void generateOrderNumber() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.orderNumber = "ORD-" + today + "-" + String.format("%06d", this.id);
    }

    public void addItem(AgencyOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void markReceived() { this.status = OrderStatus.COMPLETED; }

    public void cancel() { this.status = OrderStatus.CANCELED; }

    public static AgencyOrder create(Long agencyId) {
        return AgencyOrder.builder()
                .agencyId(agencyId)
                .status(OrderStatus.PENDING)
                .build();
    }
}