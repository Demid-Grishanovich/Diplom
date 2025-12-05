package com.datacrowd.backend.worker.dto;

import java.time.Instant;

public record WorkerStatsResponse(
        Long workerId,
        String workerEmail,
        Integer tasksCompleted,
        Double avgScore,
        Instant lastActive,
        Long pointsBalance,
        Long totalPointsEarned
) {
}
