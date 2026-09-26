package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.AuthenticationType;
import com.example.apisecurity.entity.Severity;

public class AuthenticationTest implements SecurityTest {
    public String name() { return "AuthenticationTest"; }
    public SecurityTestResult execute(ApiEndpoint endpoint, String baseUrl) {
        if (endpoint.getAuthenticationType() == AuthenticationType.NONE) return new SecurityTestResult(name(), com.example.apisecurity.entity.ResultStatus.SKIPPED, Severity.MEDIUM, "Authentication was not configured for this endpoint.", "authenticationType=NONE", "Register the expected authentication type before testing.", null, null);
        var response = HttpSecurityTestSupport.send(endpoint, baseUrl, endpoint.getMethod().name(), "", endpoint.getRequestBody());
        return HttpSecurityTestSupport.result(name(), response.status() != null && (response.status() == 401 || response.status() == 403), Severity.HIGH, "Checks that an unauthenticated request is rejected.", "HTTP status: " + response.status(), "Require authentication before processing protected requests.", response);
    }
}
