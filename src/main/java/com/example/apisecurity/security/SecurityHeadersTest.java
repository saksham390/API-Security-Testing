package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.Severity;

public class SecurityHeadersTest implements SecurityTest {
    public String name() { return "SecurityHeadersTest"; }
    public SecurityTestResult execute(ApiEndpoint endpoint, String baseUrl) {
        var response = HttpSecurityTestSupport.send(endpoint, baseUrl, "GET", "", null);
        boolean present = response.headers().containsKey("x-content-type-options") && response.headers().containsKey("x-frame-options");
        return HttpSecurityTestSupport.result(name(), present, Severity.LOW, "Checks common browser security response headers.", "X-Content-Type-Options and X-Frame-Options present: " + present, "Configure standard security response headers.", response);
    }
}
