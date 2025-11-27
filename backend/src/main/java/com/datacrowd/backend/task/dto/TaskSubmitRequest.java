package com.datacrowd.backend.task.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskSubmitRequest(
        @NotBlank
        String answerJson
) {
}
