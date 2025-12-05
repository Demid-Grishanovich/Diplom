package com.datacrowd.backend.task.dto;

import java.time.Instant;

public record TaskResponse(
        Long id,
        Long projectId,
        Long datasetId,
        String type,
        String payloadRef,
        String inputPreview,
        String status,
        Integer rewardPoints,
        Long assignedToId,
        Instant createdAt,
        Instant updatedAt
) {
}
