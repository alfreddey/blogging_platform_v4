# BLOGGING PLATFORM

A production-ready Spring Boot backend that exposes **REST** and **GraphQL** APIs for managing a blogging system. The application follows a clean layered architecture and integrates caching, validation, AOP logging, and optimized data access using MongoDB.

---

# Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Features](#features)
4. [Tech Stack](#tech-stack)
5. [Installation](#installation)
6. [Environment Configuration](#environment-configuration)
7. [Caching Configuration](#caching-configuration)
8. [API Documentation & Testing](#api-documentation--testing)
9. [Running the Application](#running-the-application)
10. [Security Note: CORS vs CSRF](#security-note-cors-vs-csrf)
10. [Contribution](#contribution)
11. [License](#license)
12. [Contact](#contact)

---

## Project Overview

This Blogging Platform backend is built with **Spring Boot 3.x** and designed using enterprise-grade best practices:

- Constructor-based Dependency Injection
- Layered architecture (Controller → Service → Repository)
- Centralized exception handling with `@ControllerAdvice`
- AOP-based logging and performance monitoring
- Pagination, sorting, and optimized query logic
- Spring Cache integration for improved read performance

The system is optimized for scalability, maintainability, and clean separation of concerns.

---

## Architecture

```
Controller Layer  →  Service Layer  →  Repository Layer  →  MongoDB
```

- **Controllers**: Handle REST & GraphQL requests
- **Services**: Business logic + transaction boundaries
- **Repositories**: Data access abstraction
- **Caching Layer**: Spring Cache abstraction
- **AOP Layer**: Logging & performance monitoring

---

## Features

- ✅ RESTful CRUD endpoints
- ✅ GraphQL queries & mutations
- ✅ Pagination & Sorting
- ✅ Bean Validation (`@Valid`, `@NotNull`, etc.)
- ✅ Centralized Exception Handling
- ✅ AOP Logging (`@Before`, `@After`, `@Around`)
- ✅ Spring Cache integration
- ✅ MongoDB integration using environment-based configuration

---

## Tech Stack

| Area | Description |
|------|------------|
| Framework | Spring Boot 3.x (Web, Validation, AOP, GraphQL, Cache) |
| Language | Java 21 |
| Database | MongoDB |
| Architecture | Layered (Controller → Service → Repository) |
| Documentation | Springdoc OpenAPI / Swagger UI |
| Build Tool | Maven |

---

## Installation

### 1️⃣ Prerequisites

- JDK 21+
- Maven
- MongoDB (local or cloud e.g. MongoDB Atlas)

---

### 2️⃣ Clone the Repository

```bash
git clone https://github.com/alfreddey/blogging-platform-v2
cd blogging-platform-v2
```

---

### 3️⃣ Build the Project

```bash
mvn clean install
```

---

## Environment Configuration

The application uses **environment variables** for secure configuration.

### MongoDB URI (Required)

The MongoDB connection string is **not hardcoded**.  
It must be provided via an environment variable:

```bash
export MONGO_URI=mongodb://localhost:27017/blogdb
```

Or for Windows (PowerShell):

```powershell
setx MONGO_URI "mongodb://localhost:27017/blogdb"
```

In `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: ${MONGO_URI}
```

This ensures:
- No credentials are committed to source control
- Secure production deployment
- Easy switching between environments

---

### Spring Profiles

The project supports:

- `dev` – Local development
- `test` – Automated testing
- `prod` – Production deployment

Run with a specific profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Caching Configuration

The application uses **Spring Cache abstraction** with an in-memory cache manager.

### Cache Manager Configuration

```java
@Bean
public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager("users", "posts");
}
```

### Cache Names

- `users`
- `posts`

### Usage

- `@Cacheable("posts")` – Cache frequently accessed blog posts
- `@Cacheable("users")` – Cache user lookups
- `@CacheEvict` – Evict cache on create/update/delete operations

### Enable Caching

```java
@EnableCaching
```

### Strategy

- Cache read-heavy operations (GET requests)
- Evict cache on write operations
- Improve response time for frequently accessed resources

---

## API Documentation & Testing

### REST API

- Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

- Tested using Postman
- Standard JSON request/response structure

---

### GraphQL

- Accessible via `/graphql`
- Test using:
    - GraphiQL
    - Altair
    - Postman (GraphQL mode)

Supports:
- Queries
- Mutations
- Pagination arguments

---

## Running the Application

After setting the `MONGO_URI`:

```bash
mvn spring-boot:run
```

Or run directly from your IDE.

---

## Security Note: CORS vs CSRF

CORS and CSRF solve different problems even though they both relate to browser security. **CORS** controls which external origins are allowed to make requests to this backend, while **CSRF** ensures that state-changing requests actually come from the intended user interaction. In this project, CORS is configured in `SecurityConfig` using `cors(cors -> cors.configurationSource(corsConfigurationSource()))`, where allowed origins, headers (including `X-CSRF-TOKEN`), and credentials are explicitly defined to permit trusted frontends. CSRF protection is enabled separately using `csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))`, which generates a token stored in a cookie and requires that token to be sent back with POST requests like `/form`. In simple terms, CORS decides *who can talk to the API*, while CSRF verifies *who actually initiated the request*, preventing malicious sites from submitting requests on behalf of authenticated users.

---

## Monitoring & Logging

The application uses AOP to:

- Log method execution
- Measure execution time
- Track performance of CRUD operations

This improves observability and debugging efficiency.

---

## Contribution

1. Fork the repository
2. Create a feature branch
   ```bash
   git checkout -b feature/your-feature
   ```
3. Commit changes
   ```bash
   git commit -m "Add meaningful feature"
   ```
4. Push and open a Pull Request

---

## License

MIT License

---

## Contact

**Alfred Nelly**  
📧 alfrednelly246@gmail.com  
