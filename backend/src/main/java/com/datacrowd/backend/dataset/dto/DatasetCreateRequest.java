package com.datacrowd.backend.dataset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DatasetCreateRequest(
        @NotBlank
        @Size(max = 500)
        String filePath,

        @Size(max = 500)
        String processedBasePath,

        @Size(max = 50)
        String type
        // version и status зададим на бэке (1, NEW)
) {
}
