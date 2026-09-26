package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.AuthenticationType;
import com.example.apisecurity.entity.Severity;

public class AuthorizationTest implements SecurityTest {
    public String name() { return "AuthorizationTest"; }
    public SecurityTestResult execute(ApiEndpoint endpoint, String baseUrl) {
        if (endpoint.getAuthenticationType() == AuthenticationType.NONE) return new SecurityTestResult(name(), com.example.apisecurity.entity.ResultStatus.SKIPPED, Severity.MEDIUM, "Authorization needs a protected endpoint to evaluate.", "authenticationType=NONE", "Configure authentication and user roles for this endpoint.", null, null);
        var response = HttpSecurityTestSupport.send(endpoint, baseUrl, endpoint.getMethod().name(), "", endpoint.getRequestBody());
        return HttpSecurityTestSupport.result(name(), response.status() != null && (response.status() == 401 || response.status() == 403), Severity.HIGH, "Checks that access is denied when no user permissions are supplied.", "HTTP status: " + response.status(), "Enforce authorization at the resource boundary.", response);
    }
}
