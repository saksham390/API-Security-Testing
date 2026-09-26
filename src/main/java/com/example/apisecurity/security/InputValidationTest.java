package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.Severity;

public class InputValidationTest implements SecurityTest {
    public String name() { return "InputValidationTest"; }
    public SecurityTestResult execute(ApiEndpoint endpoint, String baseUrl) {
        var response = HttpSecurityTestSupport.send(endpoint, baseUrl, endpoint.getMethod().name(), "?input=unexpected-test-value", endpoint.getRequestBody());
        return HttpSecurityTestSupport.result(name(), response.status() != null && response.status() < 500, Severity.MEDIUM, "Checks that unexpected but harmless input does not cause a server error.", "HTTP status: " + response.status(), "Validate and safely handle all client-controlled input.", response);
    }
}
