package com.example.apisecurity.controller;

import com.example.apisecurity.dto.ProjectResponse;
import com.example.apisecurity.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {
    @Mock ProjectService projectService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new ProjectController(projectService)).setValidator(validator).build();
    }

    @Test
    void rejectsInvalidProjectRequest() throws Exception {
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"baseUrl\":\"https://example.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createsProjectWithValidRequest() throws Exception {
        when(projectService.create(any())).thenReturn(new ProjectResponse(1L, "Demo API", "Local", "http://localhost:8081", Instant.now()));

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Demo API\",\"description\":\"Local\",\"baseUrl\":\"http://localhost:8081\"}"))
                .andExpect(status().isCreated());
    }
}
