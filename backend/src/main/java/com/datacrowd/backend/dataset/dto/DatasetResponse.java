package com.datacrowd.backend.dataset.dto;

import java.time.Instant;

public record DatasetResponse(
        Long id,
        Long projectId,
        String filePath,
        String processedBasePath,
        String type,
        Integer version,
        String status,
        Instant createdAt,
        Instant processedAt
) {
}
