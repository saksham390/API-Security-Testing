package com.example.apisecurity.controller;

import com.example.apisecurity.dto.TestResultResponse;
import com.example.apisecurity.dto.TestRunResponse;
import com.example.apisecurity.service.TestRunService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestRunController {
    private final TestRunService testRunService;

    public TestRunController(TestRunService testRunService) { this.testRunService = testRunService; }

    @PostMapping("/test-runs/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TestRunResponse run(@PathVariable Long projectId) { return testRunService.run(projectId); }

    @GetMapping("/test-runs/{id}")
    public TestRunResponse findById(@PathVariable Long id) { return testRunService.findById(id); }

    @GetMapping("/test-runs/{id}/results")
    public List<TestResultResponse> results(@PathVariable Long id) { return testRunService.results(id); }

    @GetMapping("/projects/{projectId}/test-runs")
    public List<TestRunResponse> findByProject(@PathVariable Long projectId) { return testRunService.findByProject(projectId); }
}
