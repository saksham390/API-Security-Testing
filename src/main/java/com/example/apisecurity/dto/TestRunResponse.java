package com.example.apisecurity.dto;

import com.example.apisecurity.entity.RunStatus;

import java.time.Instant;

public record TestRunResponse(Long id, Long projectId, Instant startedAt, Instant completedAt, RunStatus status) { }
