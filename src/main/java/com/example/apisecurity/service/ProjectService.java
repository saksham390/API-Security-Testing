package com.example.apisecurity.service;

import com.example.apisecurity.dto.ProjectRequest;
import com.example.apisecurity.dto.ProjectResponse;
import com.example.apisecurity.entity.Project;
import com.example.apisecurity.exception.ProjectNotFoundException;
import com.example.apisecurity.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project(null, request.name(), request.description(), request.baseUrl(), Instant.now());
        return toResponse(projectRepository.save(project));
    }

    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ProjectResponse findById(Long id) {
        return projectRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        project.setName(request.name());
        project.setDescription(request.description());
        project.setBaseUrl(request.baseUrl());
        return toResponse(projectRepository.save(project));
    }

    public void delete(Long id) {
        projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        projectRepository.deleteById(id);
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getBaseUrl(), project.getCreatedAt());
    }
}
