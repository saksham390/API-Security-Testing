package com.example.apisecurity.dto;

import com.example.apisecurity.entity.AuthenticationType;
import com.example.apisecurity.entity.HttpMethod;

public record ApiEndpointResponse(
        Long id,
        Long projectId,
        String name,
        String path,
        HttpMethod method,
        String description,
        AuthenticationType authenticationType,
        String requestBody
) { }
