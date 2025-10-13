package com.sampoom.backend.api.cart.repository;

import com.sampoom.backend.api.cart.entity.AgencyCartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyCartItemRepository extends CrudRepository<AgencyCartItem, Long> {

    List<AgencyCartItem> findByAgencyId(Long agencyId);

    Optional<AgencyCartItem> findByAgencyIdAndPartId(Long agencyId, Long partId);

    void deleteByAgencyIdAndPartId(Long agencyId, Long partId);
}
