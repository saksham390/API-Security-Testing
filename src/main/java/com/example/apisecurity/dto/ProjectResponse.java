package com.example.apisecurity.dto;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        String baseUrl,
        Instant createdAt
) {
}
