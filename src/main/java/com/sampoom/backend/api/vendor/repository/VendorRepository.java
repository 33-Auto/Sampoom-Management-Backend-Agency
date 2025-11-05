package com.sampoom.backend.api.vendor.repository;

import com.sampoom.backend.api.agency.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Agency, Long> {
}
