# Project API

REST API for managing students, schools, offers, enrollments, subjects, grades, and transcripts.

## Overview

This project is built with Spring Boot and follows a layered structure (`application`, `domain`, `infra`, `security`).
It provides:

- JWT authentication with refresh token flow
- Role-based authorization (`ALUNO`, `ESCOLA`)
- Input validation and centralized exception handling
- Swagger/OpenAPI documentation
- SQLite (default) and H2 for tests
- Redis integration for token blacklist and rate-limiting support

## Tech Stack

- Java 25
- Spring Boot 4.0.2
- Spring Security
- Spring Data JPA (Hibernate)
- SQLite (`sqlite-jdbc`) and H2 (tests)
- Redis
- MapStruct
- Lombok
- Gradle
- springdoc-openapi

## Project Structure

```text
src/main/java/com/example/projectapi
  application/   # controllers, DTOs, use cases, converters
  domain/        # entities, repositories, domain rules
  infra/         # exception handling, validation, redis, scheduler
  security/      # JWT filter/util, security config, auth support
```

## Prerequisites

- JDK 25
- Redis running locally (or configured externally)

## Running the Project

### Windows (PowerShell)

```bash
./gradlew.bat bootRun
```

### Linux/macOS

```bash
./gradlew bootRun
```

## Running Tests

### Windows

```bash
./gradlew.bat test
```

### Linux/macOS

```bash
./gradlew test
```

## API Documentation

With the app running:

- Swagger UI: `http://localhost:8080/docs`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Main Endpoints

### Auth

- `POST /api/auth/login` (public)
- `POST /api/auth/refresh` (public)
- `POST /api/auth/logout` (authenticated)

### Students (`ALUNO`)

- `POST /api/students` (public, create student)
- `GET /api/students/profile`
- `PUT /api/students/me`
- `DELETE /api/students/me`

### Schools (`ESCOLA`)

- `POST /api/schools` (public, create school)
- `GET /api/schools/profile`
- `GET /api/schools/students`
- `PUT /api/schools/me`
- `DELETE /api/schools/me`

### Offers

- `GET /api/offers` (`ALUNO`)
- `POST /api/offers` (`ESCOLA`)
- `PUT /api/offers/{id}` (`ESCOLA`)

### Inscribes

- `POST /api/inscribes` (`ALUNO`)
- `GET /api/inscribes` (`ESCOLA`)
- `PUT /api/inscribes/{id}` (`ESCOLA`)

### Subjects (`ESCOLA`)

- `POST /api/subjects`
- `GET /api/subjects`

### Transcripts

- `GET /api/transcripts` (`ALUNO`)
- `GET /api/transcripts/current` (`ALUNO`)
- `GET /api/transcripts/student/{idStudent}` (`ESCOLA`)

### Grades (`ESCOLA`)

- `POST /api/transcripts/{transcriptId}/grades`
- `PUT /api/transcripts/{transcriptId}/grades/{gradeId}`
- `DELETE /api/transcripts/{transcriptId}/grades/{gradeId}`

## Notes

- Default local DB is SQLite (`database.db`).
- Test profile uses H2 (`application-test.properties`).
- Security is stateless (JWT), so session state is not stored server-side.
