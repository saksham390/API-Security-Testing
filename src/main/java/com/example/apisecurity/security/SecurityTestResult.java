package com.example.apisecurity.security;

import com.example.apisecurity.entity.ResultStatus;
import com.example.apisecurity.entity.Severity;

public record SecurityTestResult(
        String testName,
        ResultStatus status,
        Severity severity,
        String description,
        String evidence,
        String recommendation,
        Integer responseStatus,
        Long responseTime
) { }
