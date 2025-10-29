package com.sampoom.backend.api.part.repository;

import com.sampoom.backend.api.part.projection.PartCategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartCategoryProjectionRepository extends JpaRepository<PartCategoryProjection, Long> {
}
