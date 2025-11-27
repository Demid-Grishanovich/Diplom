package com.datacrowd.backend.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(
        @NotBlank
        @Size(min = 3, max = 255)
        String name,

        @Size(max = 2000)
        String description,

        @Size(max = 50)
        String dataType
) {
}
