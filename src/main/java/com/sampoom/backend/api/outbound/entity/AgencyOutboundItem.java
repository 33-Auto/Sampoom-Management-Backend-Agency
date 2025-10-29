package com.sampoom.backend.api.outbound.entity;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.part.entity.Part;
import com.sampoom.backend.common.exception.BadRequestException;
import com.sampoom.backend.common.response.ErrorStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agency_outbound_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyOutboundItem {

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

    public void updateQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new BadRequestException(ErrorStatus.INVALID_QUANTITY);
        }

        this.quantity = newQuantity;
    }

    public static AgencyOutboundItem create(Agency agency, Part part, int quantity) {

        if (quantity <= 0) {
            throw new BadRequestException(ErrorStatus.INVALID_QUANTITY);
        }

        return AgencyOutboundItem.builder()
                .agency(agency)
                .partId(part.getId())
                .partName(part.getName())
                .partCode(part.getCode())
                .quantity(quantity)
                .build();
    }
}