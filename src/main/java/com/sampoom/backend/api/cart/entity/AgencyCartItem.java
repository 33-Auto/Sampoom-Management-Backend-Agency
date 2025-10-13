package com.sampoom.backend.api.cart.entity;

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

    private Long agencyId;
    private Long partId;
    private int quantity;

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
