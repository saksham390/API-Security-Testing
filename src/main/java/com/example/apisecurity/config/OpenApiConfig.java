package com.example.apisecurity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI apiSecurityOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API Security Testing Dashboard")
                .version("1.0")
                .description("Safe, deterministic checks for explicitly registered localhost APIs."));
    }
}
