package com.example.apisecurity.security;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityTestRegistry {
    private final List<SecurityTest> tests = List.of(
            new AuthenticationTest(), new AuthorizationTest(), new InputValidationTest(), new MissingRequiredFieldTest(),
            new InvalidMethodTest(), new BoundaryValueTest(), new SecurityHeadersTest(), new ContentTypeTest()
    );

    public List<SecurityTest> all() { return tests; }
}
