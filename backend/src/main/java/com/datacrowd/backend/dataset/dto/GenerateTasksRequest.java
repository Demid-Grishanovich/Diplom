package com.datacrowd.backend.dataset.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record GenerateTasksRequest(
        int count,
        @NotBlank
        String type,
        Integer rewardPoints
) {
}
