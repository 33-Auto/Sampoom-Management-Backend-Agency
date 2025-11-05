package com.sampoom.backend.api.part.service;

import com.sampoom.backend.api.part.dto.PartCategoryPayload;
import com.sampoom.backend.api.part.entity.Category;
import com.sampoom.backend.api.part.event.dto.PartCategoryEvent;
import com.sampoom.backend.api.part.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartCategoryEventService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void handleCategoryEvent(PartCategoryEvent event) {
        String type = event.getEventType();
        PartCategoryPayload payload = event.getPayload();

        switch (type) {
            case "PartCategoryCreated", "PartCategoryUpdated" -> handleCreateOrUpdate(payload);
            case "PartCategoryDeleted" -> handleDelete(payload.getCategoryId());
            default -> log.warn("⚠️ Unknown Category event type: {}", type);
        }
    }

    private void handleCreateOrUpdate(PartCategoryPayload payload) {
        Category category = categoryRepository.findById(payload.getCategoryId())
                .orElseGet(() -> Category.builder()
                        .id(payload.getCategoryId())
                        .build());

        category.updateFromPayload(payload);
        categoryRepository.save(category);

        log.info("✅ Saved/Updated Category: {}", category.getName());
    }

    private void handleDelete(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(
                c -> {
                    categoryRepository.delete(c);
                    log.info("🗑 Deleted category: {}", c.getName());
                },
                () -> log.warn("⚠️ Tried to delete non-existing category {}", id)
        );
    }
}
