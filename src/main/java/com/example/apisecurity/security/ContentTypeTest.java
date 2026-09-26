package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.Severity;

public class ContentTypeTest implements SecurityTest {
    public String name() { return "ContentTypeTest"; }
    public SecurityTestResult execute(ApiEndpoint endpoint, String baseUrl) {
        var response = HttpSecurityTestSupport.send(endpoint, baseUrl, "GET", "", null);
        boolean json = response.contentType().toLowerCase().contains("json");
        return HttpSecurityTestSupport.result(name(), json, Severity.LOW, "Checks that API responses declare an appropriate JSON content type.", "Content-Type: " + response.contentType(), "Return the correct Content-Type for every API response.", response);
    }
}
