package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.Severity;

public class InvalidMethodTest implements SecurityTest {
    public String name() { return "InvalidMethodTest"; }
    public SecurityTestResult execute(ApiEndpoint endpoint, String baseUrl) {
        String invalidMethod = endpoint.getMethod().name().equals("GET") ? "DELETE" : "TRACE";
        var response = HttpSecurityTestSupport.send(endpoint, baseUrl, invalidMethod, "", null);
        return HttpSecurityTestSupport.result(name(), response.status() != null && (response.status() == 400 || response.status() == 405), Severity.LOW, "Checks that an unsupported HTTP method is rejected.", "Sent " + invalidMethod + "; HTTP status: " + response.status(), "Allow only the methods documented for the route.", response);
    }
}
