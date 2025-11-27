package com.datacrowd.backend.analytics.dto;

public record ProjectAnalyticsResponse(
        Long projectId,
        String projectName,
        long totalTasks,
        long completedTasks,
        long rejectedTasks,
        long activeDatasets
) {
}
