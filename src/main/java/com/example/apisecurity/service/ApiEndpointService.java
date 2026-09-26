package com.example.apisecurity.service;

import com.example.apisecurity.dto.ApiEndpointRequest;
import com.example.apisecurity.dto.ApiEndpointResponse;
import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.Project;
import com.example.apisecurity.exception.ProjectNotFoundException;
import com.example.apisecurity.repository.ApiEndpointRepository;
import com.example.apisecurity.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiEndpointService {
    private final ApiEndpointRepository endpointRepository;
    private final ProjectRepository projectRepository;

    public ApiEndpointService(ApiEndpointRepository endpointRepository, ProjectRepository projectRepository) {
        this.endpointRepository = endpointRepository;
        this.projectRepository = projectRepository;
    }

    public ApiEndpointResponse create(Long projectId, ApiEndpointRequest request) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        ApiEndpoint endpoint = new ApiEndpoint();
        endpoint.setProject(project);
        copy(request, endpoint);
        return toResponse(endpointRepository.save(endpoint));
    }

    public List<ApiEndpointResponse> findByProject(Long projectId) {
        projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        return endpointRepository.findByProjectId(projectId).stream().map(this::toResponse).toList();
    }

    public ApiEndpointResponse findById(Long id) {
        return endpointRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Endpoint with id " + id + " was not found"));
    }

    public ApiEndpointResponse update(Long id, ApiEndpointRequest request) {
        ApiEndpoint endpoint = endpointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Endpoint with id " + id + " was not found"));
        copy(request, endpoint);
        return toResponse(endpointRepository.save(endpoint));
    }

    public void delete(Long id) {
        if (!endpointRepository.existsById(id)) {
            throw new IllegalArgumentException("Endpoint with id " + id + " was not found");
        }
        endpointRepository.deleteById(id);
    }

    private void copy(ApiEndpointRequest request, ApiEndpoint endpoint) {
        endpoint.setName(request.name());
        endpoint.setPath(request.path());
        endpoint.setMethod(request.method());
        endpoint.setDescription(request.description());
        endpoint.setAuthenticationType(request.authenticationType());
        endpoint.setRequestBody(request.requestBody());
    }

    private ApiEndpointResponse toResponse(ApiEndpoint endpoint) {
        return new ApiEndpointResponse(endpoint.getId(), endpoint.getProject().getId(), endpoint.getName(), endpoint.getPath(), endpoint.getMethod(), endpoint.getDescription(), endpoint.getAuthenticationType(), endpoint.getRequestBody());
    }
}
