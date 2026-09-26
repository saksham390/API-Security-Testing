package com.example.apisecurity.security;

import com.example.apisecurity.entity.ApiEndpoint;
import com.example.apisecurity.entity.ResultStatus;
import com.example.apisecurity.entity.Severity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

final class HttpSecurityTestSupport {
    private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();

    private HttpSecurityTestSupport() { }

    static Response send(ApiEndpoint endpoint, String baseUrl, String method, String suffix, String body) {
        try {
            URI uri = URI.create(baseUrl + endpoint.getPath().replaceAll("\\{[^/]+}", "1") + suffix);
            HttpRequest.Builder builder = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(5))
                    .header("Accept", "application/json");
            if (body == null) builder.method(method, HttpRequest.BodyPublishers.noBody());
            else builder.method(method, HttpRequest.BodyPublishers.ofString(body)).header("Content-Type", "application/json");
            long started = System.nanoTime();
            HttpResponse<String> response = CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            return new Response(response.statusCode(), response.headers().firstValue("Content-Type").orElse(""), response.headers().map(), (System.nanoTime() - started) / 1_000_000);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return new Response(null, "", java.util.Map.of(), 0L);
        } catch (IOException | IllegalArgumentException exception) {
            return new Response(null, "", java.util.Map.of(), 0L);
        }
    }

    static SecurityTestResult result(String name, boolean passed, Severity severity, String description, String evidence, String recommendation, Response response) {
        ResultStatus status = response.status() == null ? ResultStatus.ERROR : (passed ? ResultStatus.PASS : ResultStatus.FAIL);
        return new SecurityTestResult(name, status, severity, description, evidence, recommendation, response.status(), response.timeMs());
    }

    record Response(Integer status, String contentType, java.util.Map<String, java.util.List<String>> headers, Long timeMs) { }
}
