package com.datacrowd.backend.answer.dto;

import java.time.Instant;

public record AnswerResponse(
        Long id,
        Long taskId,
        Long workerId,
        String answerJson,
        Double evaluationScore,
        Boolean accepted,
        Instant createdAt
) {
}
