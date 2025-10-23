package com.sampoom.backend.api.agency.repository.outbox;

import com.sampoom.backend.api.agency.entity.outbox.AgencyOutbox;
import com.sampoom.backend.api.agency.entity.outbox.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgencyOutboxRepository extends JpaRepository<AgencyOutbox, Long> {

    List<AgencyOutbox> findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
