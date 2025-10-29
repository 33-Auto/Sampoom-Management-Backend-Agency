package com.sampoom.backend.api.order.entity;

import com.sampoom.backend.common.entity.BaseTimeEntity;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private String agencyName;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "agencyOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AgencyOrderItem> items = new ArrayList<>();

//    @PostPersist
//    public void generateOrderNumber() {
//        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        this.orderNumber = "ORD-" + today + "-" + String.format("%06d", this.id);
//    }

    @PrePersist
    public void setInitialValues() {
        if (this.orderNumber == null) {
            String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            this.orderNumber = "ORD-" + today + "-" + uuidPart;
        }
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
    }

    public void addItem(AgencyOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    // 창고 연결되면
    public void markConfirmed() {
        if (this.status != OrderStatus.PENDING) {
            throw new BadRequestException(ErrorStatus.ORDER_STATUS_INVALID);
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void markReceived() {
//        if (this.status != OrderStatus.CONFIRMED) {
//            throw new BadRequestException(ErrorStatus.ORDER_STATUS_INVALID);
//        }

        if (this.status == OrderStatus.CANCELED || this.status == OrderStatus.COMPLETED) {
            throw new BadRequestException(ErrorStatus.ORDER_STATUS_INVALID);
        }

        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status == OrderStatus.COMPLETED) {
            throw new BadRequestException(ErrorStatus.ORDER_ALREADY_COMPLETED);
        }
        this.status = OrderStatus.CANCELED;
    }

    public static AgencyOrder create(Long agencyId, String agencyName) {
        AgencyOrder order = new AgencyOrder();
        order.agencyId = agencyId;
        order.agencyName = agencyName;
        order.status = OrderStatus.PENDING;
        return order;
    }
}