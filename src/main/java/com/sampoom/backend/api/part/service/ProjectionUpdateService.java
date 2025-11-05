//package com.sampoom.backend.api.part.service;
//
//import com.sampoom.backend.api.part.event.dto.PartCategoryEvent;
//import com.sampoom.backend.api.part.event.dto.PartEvent;
//import com.sampoom.backend.api.part.event.dto.PartGroupEvent;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ProjectionUpdateService {
//
//    private final PartProjectionRepository partProjectionRepository;
//    private final PartGroupProjectionRepository partGroupProjectionRepository;
//    private final PartCategoryProjectionRepository partCategoryProjectionRepository;
//    private final ProjectionUpdater projectionUpdater;
//
//    // Part 이벤트 처리
//    @Transactional
//    public void applyPartEvent(PartEvent event) {
//        Long partId = event.getPayload().getPartId();
//        Long incomingVersion = event.getVersion() == null ? 0L : event.getVersion();
//
//        PartProjection existing = partProjectionRepository.findById(partId).orElse(null);
//        if (isIgnored(existing, event.getEventId(), incomingVersion)) return; // 멱등성 & 버전 체크
//
//        switch (event.getEventType()) {
//            case "PartCreated":
//            case "PartUpdated":
//                projectionUpdater.upsertPart(existing, event);
//                break;
//            case "PartDeleted":
//                projectionUpdater.softDeletePart(existing, event);
//                break;
//            default:
//                log.warn("Unknown Part eventType: {}", event.getEventType());
//        }
//    }
//
//    // PartGroup 이벤트 처리
//    @Transactional
//    public void applyPartGroupEvent(PartGroupEvent event) {
//        Long groupId = event.getPayload().getGroupId();
//        Long incomingVersion = event.getVersion() == null ? 0L : event.getVersion();
//
//        PartGroupProjection existing = partGroupProjectionRepository.findById(groupId).orElse(null);
//        if (isIgnored(existing, event.getEventId(), incomingVersion)) return;
//
//        switch (event.getEventType()) {
//            case "PartGroupCreated":
//
//            case "PartGroupUpdated":
//                projectionUpdater.upsertPartGroup(existing, event);
//                break;
//            case "PartGroupDeleted":
//                projectionUpdater.softDeletePartGroup(existing, event);
//                break;
//            default:
//                log.warn("Unknown PartGroup eventType: {}", event.getEventType());
//        }
//    }
//
//    // PartCategory 이벤트 처리
//    @Transactional
//    public void applyCategoryEvent(PartCategoryEvent event) {
//        Long categoryId = event.getPayload().getCategoryId();
//        Long incomingVersion = event.getVersion() == null ? 0L : event.getVersion();
//
//        PartCategoryProjection existing = partCategoryProjectionRepository.findById(categoryId).orElse(null);
//        if (isIgnored(existing, event.getEventId(), incomingVersion)) return;
//
//        switch (event.getEventType()) {
//            case "PartCategoryCreated":
//            case "PartCategoryUpdated":
//                projectionUpdater.upsertCategory(existing, event);
//                break;
//            case "PartCategoryDeleted":
//                projectionUpdater.softDeleteCategory(existing, event);
//                break;
//            default:
//                log.warn("Unknown Category eventType: {}", event.getEventType());
//        }
//    }
//
//    private boolean isIgnored(ProjectionMetadata existingProjection, String eventId, Long incomingVersion) { // ⭐️ 타입 변경
//        // 이미 null 체크는 호출부에서 했지만, 방어적으로 다시 체크합니다.
//        if (existingProjection == null) return false;
//
//        Long currentVersion = existingProjection.getVersion();
//        UUID lastEventId = existingProjection.getLastEventId();
//
//        currentVersion = currentVersion == null ? 0L : currentVersion;
//
//        // 1. 동일 이벤트 중복 방지 (eventId 체크)
//        if (lastEventId != null && lastEventId.toString().equals(eventId)) {
//            log.info("Duplicate event ignored: {}", eventId);
//            return true;
//        }
//
//        // 2. 오래된 이벤트 무시 (버전 체크)
//        if (incomingVersion != null && incomingVersion <= currentVersion) {
//            log.info("Outdated event ignored: {} (incoming: {}, current: {})",
//                    eventId, incomingVersion, currentVersion);
//            return true;
//        }
//        return false;
//    }
//}
