package com.example.apisecurity.service;

import com.example.apisecurity.entity.Project;
import com.example.apisecurity.entity.RunStatus;
import com.example.apisecurity.repository.ApiEndpointRepository;
import com.example.apisecurity.repository.ProjectRepository;
import com.example.apisecurity.repository.TestResultRepository;
import com.example.apisecurity.repository.TestRunRepository;
import com.example.apisecurity.security.SecurityTestRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestRunServiceTest {
    @Mock ProjectRepository projectRepository;
    @Mock ApiEndpointRepository endpointRepository;
    @Mock TestRunRepository testRunRepository;
    @Mock TestResultRepository resultRepository;
    @Mock SecurityTestRegistry testRegistry;
    @InjectMocks TestRunService testRunService;

    @Test
    void completesRunWhenProjectHasNoEndpoints() {
        Project project = new Project(1L, "Demo API", "Local", "http://localhost:8081", Instant.now());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(endpointRepository.findByProjectId(1L)).thenReturn(List.of());
        when(testRunRepository.save(any())).thenAnswer(invocation -> {
            var run = invocation.getArgument(0, com.example.apisecurity.entity.TestRun.class);
            run.setId(10L);
            return run;
        });

        var response = testRunService.run(1L);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.status()).isEqualTo(RunStatus.COMPLETED);
    }
}
