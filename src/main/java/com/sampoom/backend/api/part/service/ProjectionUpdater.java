package com.sampoom.backend.api.part.service;

import com.sampoom.backend.api.part.event.dto.PartCategoryEvent;
import com.sampoom.backend.api.part.event.dto.PartEvent;
import com.sampoom.backend.api.part.event.dto.PartGroupEvent;
import com.sampoom.backend.api.part.projection.PartCategoryProjection;
import com.sampoom.backend.api.part.projection.PartGroupProjection;
import com.sampoom.backend.api.part.projection.PartProjection;
import com.sampoom.backend.api.part.repository.PartCategoryProjectionRepository;
import com.sampoom.backend.api.part.repository.PartGroupProjectionRepository;
import com.sampoom.backend.api.part.repository.PartProjectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectionUpdater {

    private final PartProjectionRepository partProjectionRepository;
    private final PartGroupProjectionRepository partGroupProjectionRepository;
    private final PartCategoryProjectionRepository partCategoryProjectionRepository;

    // Part
    public void upsertPart(PartProjection existing, PartEvent event) {
        PartEvent.Payload payload = event.getPayload();

        // existing이 null이면 Builder로 새 인스턴스를 만들고, 아니면 기존 인스턴스 사용
        PartProjection projection = existing != null ? existing : PartProjection.builder().id(payload.getPartId()).build();

        // 1. 비즈니스 필드 매핑 (Setter 사용)
        projection.setCode(payload.getCode());
        projection.setName(payload.getName());
        projection.setStatus(payload.getStatus());
        projection.setGroupId(payload.getGroupId());

        // 2. 메타데이터 필드 매핑
        projection.setVersion(event.getVersion());
        projection.setLastEventId(UUID.fromString(event.getEventId()));
        projection.setSourceUpdatedAt(OffsetDateTime.parse(event.getOccurredAt()));
        projection.setDeleted(false);

        partProjectionRepository.save(projection);
    }

    public void softDeletePart(PartProjection existing, PartEvent event) {
        if (existing != null) {
            existing.setDeleted(true);

            // 삭제 이벤트일지라도 메타데이터를 최신화하여 멱등성 보장
            existing.setVersion(event.getVersion());
            existing.setLastEventId(UUID.fromString(event.getEventId()));
            existing.setSourceUpdatedAt(OffsetDateTime.parse(event.getOccurredAt()));

            partProjectionRepository.save(existing);
        }
    }

    // PartGroup
    public void upsertPartGroup(PartGroupProjection existing, PartGroupEvent event) {
        PartGroupEvent.Payload payload = event.getPayload();

        PartGroupProjection projection = existing != null ? existing : PartGroupProjection.builder().id(payload.getGroupId()).build();

        // 1. 비즈니스 필드 매핑
        projection.setCode(payload.getGroupCode());
        projection.setName(payload.getGroupName());
        projection.setCategoryId(payload.getCategoryId());

        // 2. 메타데이터 필드 매핑
        projection.setVersion(event.getVersion());
        projection.setLastEventId(UUID.fromString(event.getEventId()));
        projection.setSourceUpdatedAt(OffsetDateTime.parse(event.getOccurredAt()));
        projection.setDeleted(false);

        partGroupProjectionRepository.save(projection);
    }

    public void softDeletePartGroup(PartGroupProjection existing, PartGroupEvent event) {
        if (existing != null) {
            existing.setDeleted(true);

            // 메타데이터 최신화
            existing.setVersion(event.getVersion());
            existing.setLastEventId(UUID.fromString(event.getEventId()));
            existing.setSourceUpdatedAt(OffsetDateTime.parse(event.getOccurredAt()));

            partGroupProjectionRepository.save(existing);
        }
    }

    // PartCategory
    public void upsertCategory(PartCategoryProjection existing, PartCategoryEvent event) {
        PartCategoryEvent.Payload payload = event.getPayload();

        PartCategoryProjection projection = existing != null ? existing : PartCategoryProjection.builder().id(payload.getCategoryId()).build();

        // 1. 비즈니스 필드 매핑
        projection.setCode(payload.getCategoryCode());
        projection.setName(payload.getCategoryName());

        // 2. 메타데이터 필드 매핑
        projection.setVersion(event.getVersion());
        projection.setLastEventId(UUID.fromString(event.getEventId()));
        projection.setSourceUpdatedAt(OffsetDateTime.parse(event.getOccurredAt()));
        projection.setDeleted(false);

        partCategoryProjectionRepository.save(projection);
    }

    public void softDeleteCategory(PartCategoryProjection existing, PartCategoryEvent event) {
        if (existing != null) {
            existing.setDeleted(true);

            // 메타데이터 최신화
            existing.setVersion(event.getVersion());
            existing.setLastEventId(UUID.fromString(event.getEventId()));
            existing.setSourceUpdatedAt(OffsetDateTime.parse(event.getOccurredAt()));

            partCategoryProjectionRepository.save(existing);
        }
    }
}
