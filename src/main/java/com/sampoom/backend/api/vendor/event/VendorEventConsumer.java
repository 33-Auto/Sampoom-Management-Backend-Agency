package com.sampoom.backend.api.vendor.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampoom.backend.api.vendor.dto.VendorPayload;
import com.sampoom.backend.api.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorEventConsumer {

    private final ObjectMapper objectMapper;
    private final EventPayloadMapper eventPayloadMapper;
    private final VendorService vendorService;

    @Transactional
    @KafkaListener(topics = {"vendor-events"})
    public void consume(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            String eventType = root.get("eventType").asText();

            // payload 안에 있는 실제 데이터 꺼내기
            JsonNode payloadNode = root.get("payload");
            VendorPayload payload = objectMapper.treeToValue(payloadNode, VendorPayload.class);

            switch (eventType) {
                case "VendorCreated":
                case "VendorUpdated":
                    vendorService.createOrUpdateVendor(payload);
                    log.info("✅ Vendor saved/updated: {}", payload.getVendorName());
                    break;

                case "VendorDeleted":
                    vendorService.deleteVendor(payload.getVendorId());
                    log.info("🗑 Vendor deleted: {}", payload.getVendorName());
                    break;

                default:
                    log.warn("⚠️ Unknown vendor eventType: {}", eventType);
            }

        } catch (Exception e) {
            log.error("❌ Failed to process vendor event", e);
            throw new RuntimeException("Kafka message processing failed", e);
        }
    }
}