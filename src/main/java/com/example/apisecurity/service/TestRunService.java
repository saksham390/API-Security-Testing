package com.example.apisecurity.service;

import com.example.apisecurity.dto.TestResultResponse;
import com.example.apisecurity.dto.TestRunResponse;
import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.Project;
import com.example.apisecurity.entity.RunStatus;
import com.example.apisecurity.entity.TestResult;
import com.example.apisecurity.entity.TestRun;
import com.example.apisecurity.exception.ProjectNotFoundException;
import com.example.apisecurity.repository.ApiEndpointRepository;
import com.example.apisecurity.repository.ProjectRepository;
import com.example.apisecurity.repository.TestResultRepository;
import com.example.apisecurity.repository.TestRunRepository;
import com.example.apisecurity.security.SecurityTest;
import com.example.apisecurity.security.SecurityTestRegistry;
import com.example.apisecurity.security.SecurityTestResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.net.URI;
import java.util.List;

@Service
public class TestRunService {
    private final ProjectRepository projectRepository;
    private final ApiEndpointRepository endpointRepository;
    private final TestRunRepository testRunRepository;
    private final TestResultRepository resultRepository;
    private final SecurityTestRegistry testRegistry;

    public TestRunService(ProjectRepository projectRepository, ApiEndpointRepository endpointRepository, TestRunRepository testRunRepository, TestResultRepository resultRepository, SecurityTestRegistry testRegistry) {
        this.projectRepository = projectRepository;
        this.endpointRepository = endpointRepository;
        this.testRunRepository = testRunRepository;
        this.resultRepository = resultRepository;
        this.testRegistry = testRegistry;
    }

    @Transactional
    public TestRunResponse run(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        verifyLocalhost(project.getBaseUrl());
        TestRun run = new TestRun();
        run.setProject(project);
        run.setStartedAt(Instant.now());
        run.setStatus(RunStatus.RUNNING);
        run = testRunRepository.save(run);

        for (ApiEndpoint endpoint : endpointRepository.findByProjectId(projectId)) {
            for (SecurityTest test : testRegistry.all()) {
                SecurityTestResult checked = test.execute(endpoint, project.getBaseUrl());
                TestResult result = new TestResult();
                result.setTestRun(run);
                result.setEndpoint(endpoint);
                result.setTestName(checked.testName());
                result.setStatus(checked.status());
                result.setSeverity(checked.severity());
                result.setDescription(checked.description());
                result.setEvidence(checked.evidence());
                result.setRecommendation(checked.recommendation());
                result.setResponseStatus(checked.responseStatus());
                result.setResponseTime(checked.responseTime());
                resultRepository.save(result);
            }
        }
        run.setCompletedAt(Instant.now());
        run.setStatus(RunStatus.COMPLETED);
        return toResponse(testRunRepository.save(run));
    }

    @Transactional(readOnly = true)
    public TestRunResponse findById(Long id) {
        return testRunRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Test run with id " + id + " was not found"));
    }

    @Transactional(readOnly = true)
    public List<TestResultResponse> results(Long id) {
        findById(id);
        return resultRepository.findByTestRunId(id).stream().map(this::toResultResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TestRunResponse> findByProject(Long projectId) {
        projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        return testRunRepository.findByProjectIdOrderByStartedAtDesc(projectId).stream().map(this::toResponse).toList();
    }

    private TestRunResponse toResponse(TestRun run) { return new TestRunResponse(run.getId(), run.getProject().getId(), run.getStartedAt(), run.getCompletedAt(), run.getStatus()); }
    private TestResultResponse toResultResponse(TestResult result) { return new TestResultResponse(result.getId(), result.getTestRun().getId(), result.getEndpoint().getId(), result.getTestName(), result.getStatus(), result.getSeverity(), result.getDescription(), result.getEvidence(), result.getRecommendation(), result.getResponseStatus(), result.getResponseTime()); }

    private void verifyLocalhost(String baseUrl) {
        URI uri = URI.create(baseUrl);
        if (!"localhost".equalsIgnoreCase(uri.getHost())) {
            throw new IllegalArgumentException("Security tests are limited to localhost APIs");
        }
    }
}
