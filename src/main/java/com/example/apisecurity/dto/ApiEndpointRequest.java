package com.example.apisecurity.dto;

import com.example.apisecurity.entity.AuthenticationType;
import com.example.apisecurity.entity.HttpMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ApiEndpointRequest(
        @NotNull(message = "HTTP method is required") HttpMethod method,
        @NotBlank(message = "Endpoint name is required") @Size(max = 100) String name,
        @NotBlank(message = "Endpoint path is required") @Pattern(regexp = "/.*", message = "Endpoint path must start with /") String path,
        @Size(max = 500) String description,
        @NotNull(message = "Authentication type is required") AuthenticationType authenticationType,
        String requestBody
) { }
