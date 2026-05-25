# 🎛️ API Gateway

`api-gateway` is the single entry point and traffic orchestrator for the **Atlas Supply Chain Platform**. Built using **Spring Cloud Gateway**, it securely routes incoming client requests to their appropriate downstream microservices, handles authentication filters, and provides centralized logging.

---

## 📖 Overview

Instead of client applications directly talking to multiple microservices on separate ports, all requests are funneled through the API Gateway on port `8080`. The gateway leverages the `discovery-server` (Eureka) to resolve service addresses dynamically and performs client-side load balancing.

---

## ⚡ Key Features

- **Centralized Routing** — Decouples client apps from backend topology. Routes are mapped as:
  - `/api/auth/**` ➔ `identity-service` (auth, token caching)
  - `/api/orders/**` ➔ `order-service` (JWT validation enabled)
  - `/api/inventory/**` ➔ `inventory-service` (stock and catalog)
- **Authentication Gateway Filter** — A custom `JwtAuthenticationFilter` intercepts requests to protected routes, parses the authorization header, validates the JWT, and extracts user context before forwarding.
- **Request Rewriting** — Dynamically strips prefix path patterns (e.g., rewriting `/api/orders/xyz` to `/orders/xyz` downstream).
- **Observability** — Built-in tracing (Zipkin) propagates trace and span headers to all downstream services, enabling end-to-end latency monitoring.

---

## ⚙️ Routing Configuration

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: identity-service
          uri: lb://identity-service
          predicates:
            - Path=/api/auth/**
          filters:
            - RewritePath=/api/(?<segment>.*), /${segment}

        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
          filters:
            - JwtAuthenticationFilter
            - RewritePath=/api/(?<segment>.*), /${segment}

        - id: inventory-service
          uri: lb://inventory-service
          predicates:
            - Path=/api/inventory/**
          filters:
            - RewritePath=/api/(?<segment>.*), /${segment}
```

---

## 🔒 Custom Security Filter

Protected endpoints (like `/api/orders/**`) pass through our `JwtAuthenticationFilter`. 
If a request is missing the `Authorization: Bearer <token>` header or contains an invalid/expired JWT, the gateway automatically rejects the request with an `HTTP 401 Unauthorized` status without passing it downstream.

> [!IMPORTANT]
> The gateway does *not* query a database directly to validate tokens. It performs stateless cryptographic signature verification of the JWT. For high-security validation, token blacklists are managed using Redis.

---

## 🚀 Running Locally

### Pre-requisites
- JDK 17
- Maven 3.9+
- Running `discovery-server` (Port 8761)

### Commands
1. Navigate to the directory:
   ```bash
   cd api-gateway
   ```
2. Build the JAR:
   ```bash
   mvn clean package -DskipTests
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
