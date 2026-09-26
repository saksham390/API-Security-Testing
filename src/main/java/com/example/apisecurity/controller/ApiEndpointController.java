package com.example.apisecurity.controller;

import com.example.apisecurity.dto.ApiEndpointRequest;
import com.example.apisecurity.dto.ApiEndpointResponse;
import com.example.apisecurity.service.ApiEndpointService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiEndpointController {
    private final ApiEndpointService endpointService;

    public ApiEndpointController(ApiEndpointService endpointService) {
        this.endpointService = endpointService;
    }

    @PostMapping("/projects/{projectId}/endpoints")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiEndpointResponse create(@PathVariable Long projectId, @Valid @RequestBody ApiEndpointRequest request) {
        return endpointService.create(projectId, request);
    }

    @GetMapping("/projects/{projectId}/endpoints")
    public List<ApiEndpointResponse> findByProject(@PathVariable Long projectId) {
        return endpointService.findByProject(projectId);
    }

    @GetMapping("/endpoints/{id}")
    public ApiEndpointResponse findById(@PathVariable Long id) {
        return endpointService.findById(id);
    }

    @PutMapping("/endpoints/{id}")
    public ApiEndpointResponse update(@PathVariable Long id, @Valid @RequestBody ApiEndpointRequest request) {
        return endpointService.update(id, request);
    }

    @DeleteMapping("/endpoints/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        endpointService.delete(id);
    }
}
