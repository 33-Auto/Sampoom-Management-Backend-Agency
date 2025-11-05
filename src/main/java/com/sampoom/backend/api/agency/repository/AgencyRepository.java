package com.sampoom.backend.api.agency.repository;

import com.sampoom.backend.api.agency.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
}
