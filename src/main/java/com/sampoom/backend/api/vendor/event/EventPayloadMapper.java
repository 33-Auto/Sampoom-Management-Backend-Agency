package com.sampoom.backend.api.vendor.event;

import com.sampoom.backend.api.vendor.dto.VendorPayload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventPayloadMapper {

    private static final Map<String, Class<?>> payloadTypeMap = new HashMap<>();

    static {
        payloadTypeMap.put("VendorCreated", VendorPayload.class);
        payloadTypeMap.put("VendorUpdated", VendorPayload.class);
        payloadTypeMap.put("VendorDeleted", VendorPayload.class);
    }

    public Class<?> getPayloadClass(String eventType) {
        return payloadTypeMap.getOrDefault(eventType, Object.class);
    }
}