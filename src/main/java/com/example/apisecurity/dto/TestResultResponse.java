package com.example.apisecurity.dto;

import com.example.apisecurity.entity.ResultStatus;
import com.example.apisecurity.entity.Severity;

public record TestResultResponse(Long id, Long testRunId, Long endpointId, String testName, ResultStatus status, Severity severity, String description, String evidence, String recommendation, Integer responseStatus, Long responseTime) { }
