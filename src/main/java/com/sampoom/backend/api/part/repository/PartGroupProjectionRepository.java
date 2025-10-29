package com.sampoom.backend.api.part.repository;

import com.sampoom.backend.api.part.projection.PartGroupProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartGroupProjectionRepository extends JpaRepository<PartGroupProjection, Long> {
}
