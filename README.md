# API Security Testing Dashboard

A beginner-friendly Spring Boot monolith for safe, deterministic security checks against APIs that you own or are authorized to test.

> Version 1 intentionally supports localhost APIs only. It does not scan the internet, execute shell commands, steal credentials, perform denial-of-service actions, or exploit real systems.

## Features

- Project registration and CRUD management
- API endpoint registration with HTTP method and authentication metadata
- Eight predefined checks: authentication, authorization, input validation, missing fields, invalid methods, boundary values, security headers, and content type
- Persisted test runs and readable results with status and severity
- PostgreSQL persistence through Spring Data JPA
- Bean Validation and consistent JSON error responses
- Swagger/OpenAPI documentation
- Responsive browser dashboard
- Docker Compose deployment with PostgreSQL

## Technology

Java 21, Spring Boot 3.5, Maven, Spring Web, Spring Data JPA, PostgreSQL, Spring Security, JUnit 5, Mockito, MockMvc, Swagger/OpenAPI, and Docker.

## Quick Start with Docker

Prerequisites:

- Java 21
- Maven 3.9+
- Docker Desktop

Build the application and start the database and app:

```powershell
mvn clean package -DskipTests
docker compose up --build
```

Open the dashboard at http://localhost:8080.

Swagger UI is available at http://localhost:8080/swagger-ui.html.

Stop the services with:

```powershell
docker compose down
```

PostgreSQL data is stored in the Docker volume `postgres_data`.

## Local Development

Start PostgreSQL using Docker, then run Spring Boot from the project root:

```powershell
docker compose up db -d
mvn spring-boot:run
```

The default database settings are in `src/main/resources/application.properties` and can be overridden with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.

## API Workflow

1. Create a project with `POST /api/projects`.
2. Register an endpoint with `POST /api/projects/{projectId}/endpoints`.
3. Start checks with `POST /api/test-runs/{projectId}`.
4. Read the run with `GET /api/test-runs/{id}`.
5. Read detailed results with `GET /api/test-runs/{id}/results`.

Example project request:

```json
{
	"name": "Demo E-Commerce API",
	"description": "Local API for security testing",
	"baseUrl": "http://localhost:8081"
}
```

Example endpoint request:

```json
{
	"name": "Get user",
	"path": "/api/users/{id}",
	"method": "GET",
	"description": "Fetches one user",
	"authenticationType": "NONE",
	"requestBody": ""
}
```

## Testing

Run the automated tests:

```powershell
mvn clean test
```

The test suite covers project creation, project retrieval, invalid input, MockMvc controller behavior, and test-run creation.

## Beginner Architecture

- **Controller** receives HTTP requests and returns JSON.
- **Service** contains application rules and coordinates work.
- **Repository** reads and writes entities through JPA.
- **Entity** maps Java objects to PostgreSQL tables.
- **DTO** defines the JSON contract without exposing persistence objects directly.
- **Dependency injection** lets Spring provide services and repositories to constructors.

The database relationship is:

```text
Project
	+-- ApiEndpoint
	+-- TestRun
				+-- TestResult
```

## Safety Boundary

Project validation accepts only `localhost` base URLs. Test execution checks the host again immediately before sending requests. All checks use bounded, predefined requests and store network failures as `ERROR`.

## License

This project is provided for learning and authorized defensive testing.
