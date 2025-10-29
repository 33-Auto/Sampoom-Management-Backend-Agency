package com.sampoom.backend.api.part.projection;

import java.util.UUID;

public interface ProjectionMetadata {
    Long getId();
    Long getVersion();
    UUID getLastEventId();
}