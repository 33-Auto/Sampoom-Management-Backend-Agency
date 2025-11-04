package com.sampoom.backend.api.vendor.repository;

import com.sampoom.backend.api.vendor.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
