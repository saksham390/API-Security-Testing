package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.HttpMethod;
import com.example.apisecurity.entity.Severity;

public class MissingRequiredFieldTest implements SecurityTest {
    public String name() { return "MissingRequiredFieldTest"; }
    public SecurityTestResult execute(ApiEndpoint endpoint, String baseUrl) {
        if (endpoint.getRequestBody() == null || endpoint.getRequestBody().isBlank() || endpoint.getMethod() == HttpMethod.GET) return new SecurityTestResult(name(), com.example.apisecurity.entity.ResultStatus.SKIPPED, Severity.MEDIUM, "No JSON request body was configured for this endpoint.", "requestBody is empty", "Configure an example body to test required fields.", null, null);
        var response = HttpSecurityTestSupport.send(endpoint, baseUrl, endpoint.getMethod().name(), "", "{}");
        return HttpSecurityTestSupport.result(name(), response.status() != null && response.status() >= 400 && response.status() < 500, Severity.MEDIUM, "Checks that a body missing required fields is rejected as client input.", "HTTP status: " + response.status(), "Validate required fields and return a client error.", response);
    }
}
