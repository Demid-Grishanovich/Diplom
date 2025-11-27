package com.datacrowd.backend.project.dto;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        String dataType,
        String status,
        Long ownerId,
        String ownerEmail,
        Instant createdAt,
        Instant updatedAt
) {
}
