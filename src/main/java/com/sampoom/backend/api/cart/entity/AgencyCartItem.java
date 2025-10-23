package com.sampoom.backend.api.cart.entity;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.partread.entity.Part;
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
@Table(name = "agency_cart_item")
public class AgencyCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    private Long partId;

    private String partName;

    private String partCode;

    private int quantity;

    public void addQuantity(int quantity) {
        if (quantity <= 0) {
            throw new BadRequestException(ErrorStatus.INVALID_QUANTITY);
        }
        this.quantity += quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new BadRequestException(ErrorStatus.INVALID_QUANTITY);
        }
        this.quantity = quantity;
    }

    public static AgencyCartItem create(Agency agency, Part part, int quantity) {
        return AgencyCartItem.builder()
                .agency(agency)
                .partId(part.getId())
                .partName(part.getName())
                .partCode(part.getCode())
                .quantity(quantity)
                .build();
    }
}
