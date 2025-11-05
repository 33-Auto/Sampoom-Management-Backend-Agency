package com.sampoom.backend.api.part.service;

import com.sampoom.backend.api.part.dto.PartPayload;
import com.sampoom.backend.api.part.entity.Part;
import com.sampoom.backend.api.part.entity.Category;
import com.sampoom.backend.api.part.entity.PartGroup;
import com.sampoom.backend.api.part.event.dto.PartEvent;
import com.sampoom.backend.api.part.repository.CategoryRepository;
import com.sampoom.backend.api.part.repository.PartGroupRepository;
import com.sampoom.backend.api.part.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartEventService {

    private final PartRepository partRepository;
    private final PartGroupRepository groupRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void handlePartEvent(PartEvent event) {
        String type = event.getEventType();
        PartPayload payload = event.getPayload();

        switch (type) {
            case "PartCreated", "PartUpdated" -> handleCreateOrUpdate(payload);
            case "PartDeleted" -> handleDelete(payload.getPartId());
            default -> log.warn("⚠️ Unknown Part event type: {}", type);
        }
    }

    private void handleCreateOrUpdate(PartPayload payload) {

        // 🚨 수정 1: Category와 Group이 DB에 없는 경우 트랜잭션 롤백을 피하고 로직 중단
        if (!groupRepository.existsById(payload.getGroupId())) {
            log.error("❌ Part Group ID {} does not exist. Skipping event processing.", payload.getGroupId());
            return;
        }
        if (!categoryRepository.existsById(payload.getCategoryId())) {
            log.error("❌ Category ID {} does not exist. Skipping event processing.", payload.getCategoryId());
            return;
        }

        Part part = partRepository.findById(payload.getPartId())
                .orElseGet(() -> Part.builder().id(payload.getPartId()).build());

        part.updateFromPayload(payload);
        partRepository.save(part);
        log.info("✅ Saved/Updated part: {}", part.getName());
    }

    private void handleDelete(Long id) {
        partRepository.findById(id).ifPresentOrElse(
                p -> {
                    partRepository.delete(p);
                    log.info("🗑 Deleted part: {}", p.getName());
                },
                () -> log.warn("⚠️ Tried to delete non-existing part {}", id)
        );
    }
}
