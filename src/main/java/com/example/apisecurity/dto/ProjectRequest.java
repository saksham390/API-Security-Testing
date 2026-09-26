package com.example.apisecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
        @NotBlank(message = "Project name is required")
        @Size(max = 100, message = "Project name must be 100 characters or fewer")
        String name,

        @Size(max = 500, message = "Description must be 500 characters or fewer")
        String description,

        @NotBlank(message = "Base URL is required")
        @Pattern(regexp = "https?://localhost(:[0-9]{1,5})?(/.*)?", message = "Base URL must point to localhost")
        String baseUrl
) {
}
