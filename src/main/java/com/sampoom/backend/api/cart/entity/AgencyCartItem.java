package com.sampoom.backend.api.cart.entity;

import com.sampoom.backend.api.agency.entity.Agency;
import com.sampoom.backend.api.partread.entity.Part;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private Part part;

    private int quantity;

    public void updateQuantity(int quantity) {
        this.quantity += quantity;
    }
}
