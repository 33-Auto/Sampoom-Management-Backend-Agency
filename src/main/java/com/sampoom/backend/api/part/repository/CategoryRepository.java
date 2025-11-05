package com.sampoom.backend.api.part.repository;

import com.sampoom.backend.api.part.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCode(String code);
}
