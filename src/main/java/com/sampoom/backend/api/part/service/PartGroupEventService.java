package com.sampoom.backend.api.part.service;

import com.sampoom.backend.api.part.dto.PartGroupPayload;
import com.sampoom.backend.api.part.entity.Category;
import com.sampoom.backend.api.part.entity.PartGroup;
import com.sampoom.backend.api.part.event.dto.PartGroupEvent;
import com.sampoom.backend.api.part.repository.CategoryRepository;
import com.sampoom.backend.api.part.repository.PartGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartGroupEventService {

    private final PartGroupRepository groupRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void handleGroupEvent(PartGroupEvent event) {
        String type = event.getEventType();
        PartGroupPayload payload = event.getPayload(); // ✅ 수정됨

        switch (type) {
            case "PartGroupCreated", "PartGroupUpdated" -> handleCreateOrUpdate(payload);
            case "PartGroupDeleted" -> handleDelete(payload.getGroupId());
            default -> log.warn("⚠️ Unknown Group event type: {}", type);
        }
    }

    private void handleCreateOrUpdate(PartGroupPayload payload) { // ✅ 수정됨
        Category category = categoryRepository.findById(payload.getCategoryId()).orElse(null);

        PartGroup group = groupRepository.findById(payload.getGroupId())
                .orElseGet(() -> PartGroup.builder().id(payload.getGroupId()).build());

        group.updateFromPayload(payload);
        groupRepository.save(group);
        log.info("✅ Saved/Updated PartGroup: {}", group.getName());
    }

    private void handleDelete(Long id) {
        groupRepository.findById(id).ifPresentOrElse(
                g -> {
                    groupRepository.delete(g);
                    log.info("🗑 Deleted group: {}", g.getName());
                },
                () -> log.warn("⚠️ Tried to delete non-existing group {}", id)
        );
    }
}
