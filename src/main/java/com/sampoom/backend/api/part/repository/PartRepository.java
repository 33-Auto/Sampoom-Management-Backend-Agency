package com.sampoom.backend.api.part.repository;

import com.sampoom.backend.api.part.entity.Part;
import com.sampoom.backend.api.part.entity.PartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {

    List<Part> findByGroupId(Long groupId);

    List<Part> findByGroupIdAndStatus(Long groupId, PartStatus status);

    @Query("SELECT p FROM Part p " +
            "WHERE (p.code LIKE %:keyword% OR p.name LIKE %:keyword%) " +
            "AND p.status = 'ACTIVE'")
    List<Part> searchByKeyword(@Param("keyword") String keyword);
}
