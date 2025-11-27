package com.datacrowd.backend.analytics.dto;

public record GlobalAnalyticsResponse(
        long totalUsers,
        long totalProjects,
        long totalDatasets,
        long totalTasks,
        long totalAnswers
) {
}
