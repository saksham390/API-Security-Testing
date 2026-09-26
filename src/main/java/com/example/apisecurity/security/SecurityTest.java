package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;

public interface SecurityTest {
    String name();
    SecurityTestResult execute(ApiEndpoint endpoint, String baseUrl);
}
