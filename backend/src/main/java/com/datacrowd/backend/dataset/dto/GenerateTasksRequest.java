package com.datacrowd.backend.dataset.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record GenerateTasksRequest(
        @Min(1)
        int count,
        @NotBlank
        String type
) {
}
