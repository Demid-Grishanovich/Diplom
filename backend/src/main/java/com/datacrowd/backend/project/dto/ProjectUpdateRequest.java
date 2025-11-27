package com.datacrowd.backend.project.dto;

import jakarta.validation.constraints.Size;

public record ProjectUpdateRequest(
        @Size(min = 3, max = 255)
        String name,

        @Size(max = 2000)
        String description,

        @Size(max = 50)
        String dataType,

        @Size(max = 30)
        String status    // например, можно менять ACTIVE -> ARCHIVED и т.п.
) {
}
