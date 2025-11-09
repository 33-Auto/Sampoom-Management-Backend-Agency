package com.sampoom.backend.api.agency.repository;

import com.sampoom.backend.api.agency.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long> {

    Optional<Agency> findByCode(String code);

}
