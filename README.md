# UniMart Backend

Spring Boot REST API backend for UniMart, a university marketplace platform built as part of the UniMart Monolithic Full-Stack Lab Series.

## Tech Stack

- Java 21 (LTS)
- Spring Boot 4.1.0
- Spring Data JPA + Hibernate
- Spring Security
- MySQL 8.0 (via Flyway migrations)
- Maven (with Maven Wrapper)

## Prerequisites

- JDK 21
- MySQL Server running locally
- IntelliJ IDEA (recommended) or any Java IDE

## Getting Started

1. Clone this repository
2. Create a MySQL database named `unimart` and a least-privilege user account
3. Copy `.env.example` to `.env.local` (or configure environment variables directly in your IDE run configuration) and fill in real values for `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_ACCESS_MINUTES`, `APP_ALLOWED_ORIGINS`, `SERVER_PORT`, `SPRING_PROFILES_ACTIVE`
4. Run the application: `./mvnw spring-boot:run`
5. Verify it started correctly:
   - Health check: `http://localhost:8080/actuator/health`
   - Public ping: `http://localhost:8080/api/v1/public/ping`

## Database Migrations

Schema changes are managed with Flyway. Migration files live in `src/main/resources/db/migration`. Flyway runs migrations automatically on application startup.

## Project Structure

Package-by-feature MVC layout under `lk.ac.kln.unimartbackend`:
- `common` - shared API utilities, exception handling, validation
- `config` - application-wide configuration
- `security` - authentication and authorization
- `auth`, `listing`, `review`, `order`, `notification` - feature modules, each with controller, dto, entity, repository, service layers

## Security Note

Never commit real database passwords, JWT secrets, or other credentials. Use environment variables or your IDE's run configuration to supply these locally.
