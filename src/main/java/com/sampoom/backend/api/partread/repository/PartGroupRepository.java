package com.sampoom.backend.api.partread.repository;

import com.sampoom.backend.api.partread.entity.Category;
import com.sampoom.backend.api.partread.entity.PartGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartGroupRepository extends JpaRepository<PartGroup, Long> {

    List<PartGroup> findByCategoryId(Long categoryId);

    Optional<PartGroup> findByCodeAndCategoryId(String code, Long categoryId);
}

