# 🔐 Identity Service

`identity-service` is the centralized authentication, authorization, and user management service for the **Atlas Supply Chain Platform**. Built using **Spring Boot**, **Spring Security**, and **JSON Web Tokens (JWT)**, it ensures secure access control across all services.

---

## 📖 Overview

The Identity Service handles credentials securely, validates identities, and issues cryptographically signed JWTs that clients include in subsequent API requests. It supports stateless authentication at the microservice level while keeping user records stored in a secure PostgreSQL database.

---

## ⚡ Key Features

- **User Authentication** — Cryptographic password hashing and verified login endpoints.
- **JWT Issuance** — Generates secure JSON Web Tokens with user roles, expiration timestamps, and HMAC-SHA256 signatures.
- **Service Verification** — Exposes validation endpoints for other internal microservices to verify the signature and integrity of tokens.
- **Stateless Authorization** — Utilizes database-backed user roles to provide fine-grained access control (e.g., `ROLE_USER`, `ROLE_ADMIN`).
- **High Performance** — Integrates Redis to store and manage active/invalidated token sessions efficiently.

---

## ⚙️ Configuration & Ports

- **Port:** `8082`
- **Database:** PostgreSQL (`identity_db`)
- **Caching:** Redis

### Core properties in `application.yml`
```yaml
server:
  port: 8082

spring:
  application:
    name: identity-service
  datasource:
    url: jdbc:postgresql://localhost:7778/identity_db
  jpa:
    hibernate:
      ddl-auto: update

jwt:
  secret: 5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
  expiration: 3600000 # 1 hour in milliseconds
```

---

## 📚 API Reference

All client requests should go through the **API Gateway** on port `8080`.

### 1. Register User
- **URL:** `POST /api/auth/register`
- **Request Body:**
  ```json
  {
    "username": "supply_chain_manager",
    "password": "SecurePassword123",
    "email": "manager@atlas.com"
  }
  ```
- **Response:** `200 OK` (User registered successfully)

### 2. Login (Get JWT Token)
- **URL:** `POST /api/auth/token`
- **Request Body:**
  ```json
  {
    "username": "supply_chain_manager",
    "password": "SecurePassword123"
  }
  ```
- **Response:**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBwbHlfY2hhaW5fbWFuYWdlciIsImV4cCI6MTY4NDc2...",
    "expiresIn": 3600
  }
  ```

### 3. Validate Token (Internal Downstream)
- **URL:** `GET /api/auth/validate?token=<jwt>`
- **Response:** `200 OK` (Token is valid)

---

## 🚀 Running Locally

### Pre-requisites
- JDK 17
- Maven 3.9+
- PostgreSQL server (Port 7778 or local configuration)
- Redis server (Port 6379)

### Commands
1. Navigate to the directory:
   ```bash
   cd identity-service
   ```
2. Build the JAR:
   ```bash
   mvn clean package -DskipTests
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

> [!TIP]
> Access the Swagger documentation for this service at [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) to explore all interactive endpoints and schemas.
