package com.sampoom.backend.api.part.repository;

import com.sampoom.backend.api.part.projection.PartProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartProjectionRepository extends JpaRepository<PartProjection, Long> {
}
