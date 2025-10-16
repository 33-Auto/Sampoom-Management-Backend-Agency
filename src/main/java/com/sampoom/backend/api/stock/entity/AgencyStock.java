package com.sampoom.backend.api.stock.entity;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.common.entitiy.BaseTimeEntity;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "agency_stock",
        uniqueConstraints = @UniqueConstraint(columnNames = {"agency_id", "part_id"})
)
public class AgencyStock extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    private Long partId;

    private int quantity;

    @Version
    private Long version;

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity) {
        int restQuantity = this.quantity - quantity;
        if (restQuantity < 0) {
            throw new BadRequestException(ErrorStatus.STOCK_INSUFFICIENT);
        }
        this.quantity = restQuantity;
    }

     public static AgencyStock create(Agency agency, Long partId, int quantity) {
         return AgencyStock.builder()
                 .agency(agency)
                 .partId(partId)
                 .quantity(quantity)
                 .build();
     }

}
