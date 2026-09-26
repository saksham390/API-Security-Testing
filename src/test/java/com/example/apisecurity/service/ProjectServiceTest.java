package com.example.apisecurity.service;

import com.example.apisecurity.dto.ProjectRequest;
import com.example.apisecurity.entity.Project;
import com.example.apisecurity.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock ProjectRepository projectRepository;
    @InjectMocks ProjectService projectService;

    @Test
    void createsProject() {
        Project saved = new Project(1L, "Demo API", "Local API", "http://localhost:8081", Instant.now());
        when(projectRepository.save(any(Project.class))).thenReturn(saved);

        var response = projectService.create(new ProjectRequest("Demo API", "Local API", "http://localhost:8081"));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Demo API");
    }

    @Test
    void retrievesProject() {
        Project project = new Project(1L, "Demo API", "Local API", "http://localhost:8081", Instant.now());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertThat(projectService.findById(1L).baseUrl()).isEqualTo("http://localhost:8081");
    }
}
