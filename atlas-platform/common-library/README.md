# 📚 Common Library

`common-library` is a shared Maven utility module for the **Atlas Supply Chain Platform**. It encapsulates cross-cutting concerns, shared Data Transfer Objects (DTOs), domain events, custom exceptions, and global exception handlers to keep individual microservices DRY (Don't Repeat Yourself).

---

## 📖 Overview

Instead of duplicating utility code, standard error response classes, or domain-event representations across each microservice directory, they are maintained centrally inside this library. Any change made here becomes immediately available to all services upon packaging the project.

---

## ⚡ Shared Components

### 1. Global Exception Handling
- **`GlobalExceptionHandler.java`** — Centralized controller advice annotated with `@RestControllerAdvice`.
- Intercepts default Spring validation exceptions (`MethodArgumentNotValidException`) and maps them to clean validation responses.
- Catches general `Exception.class` occurrences and translates them to unified `ErrorResponse` objects with standard server error formats.

### 2. Standardized DTOs
- **`ErrorResponse.java`** — Standard response structure for any API failure.
- **Fields:**
  - `timestamp` (Local date/time of error)
  - `status` (HTTP status code)
  - `error` (Short error classification)
  - `message` (Developer or user-facing reason)
  - `details` (Validation errors map, e.g., field-level messages)

---

## 🛠️ Usage & Installation

Because `common-library` is a local Maven dependency and not hosted on a public Maven repository (like Maven Central), it **MUST** be compiled and installed to your local `.m2` repository before compiling dependent services.

### Installation Command
From the root directory of the Atlas project:
```bash
mvn clean install -DskipTests
```
*This command compiles the library and installs the resulting `common-library-1.0-SNAPSHOT.jar` to your machine's local `.m2` repository.*

### Including in other Microservices
To use these shared classes in another microservice (e.g. `order-service`), add the following dependency in its `pom.xml`:

```xml
<dependency>
    <groupId>com.atlas</groupId>
    <artifactId>common-library</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

---

## 🚀 Development Guidelines

- **Keep it lightweight** — Avoid adding heavy business logic or microservice-specific classes to this library.
- **Maintain backward compatibility** — Be extremely cautious when renaming or deleting fields in shared DTOs/Exceptions, as it can break multiple downstream builds.
- **Keep it thread-safe** — Ensure any utility methods or helper classes are stateless and thread-safe.
