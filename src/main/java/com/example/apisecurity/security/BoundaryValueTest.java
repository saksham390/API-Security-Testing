package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.Severity;

public class BoundaryValueTest implements SecurityTest {
    public String name() { return "BoundaryValueTest"; }
    public SecurityTestResult execute(ApiEndpoint endpoint, String baseUrl) {
        var response = HttpSecurityTestSupport.send(endpoint, baseUrl, endpoint.getMethod().name(), "?limit=-1", endpoint.getRequestBody());
        return HttpSecurityTestSupport.result(name(), response.status() != null && response.status() < 500, Severity.LOW, "Checks a simple numeric boundary value without stressing the service.", "Sent limit=-1; HTTP status: " + response.status(), "Reject invalid ranges with a clear client error.", response);
    }
}
