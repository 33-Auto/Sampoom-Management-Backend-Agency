package com.sampoom.backend.api.outbound.entity;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.partread.entity.Part;
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
        this.quantity = newQuantity;
    }

    public static AgencyOutboundItem create(Agency agency, Part part, int quantity) {
        return AgencyOutboundItem.builder()
                .agency(agency)
                .partId(part.getId())
                .partName(part.getName())
                .partCode(part.getCode())
                .quantity(quantity)
                .build();
    }
}