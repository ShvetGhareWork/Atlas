<div align="center">

# ⚡ Atlas

### A production-grade, cloud-native **Inventory & Supply Chain Management Platform**
### built with a Java Spring Boot microservices architecture.

[![CI](https://github.com/YOUR_USERNAME/atlas/actions/workflows/maven.yml/badge.svg)](https://github.com/YOUR_USERNAME/atlas/actions)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

</div>

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Services](#-services)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Observability](#-observability)
- [Roadmap](#-roadmap)

---

## 🌐 Overview

**Atlas** is a fully distributed supply chain backend that handles the complete lifecycle of inventory — from procurement and stock management through to order fulfilment and shipment tracking. It is designed to mirror real-world enterprise architecture patterns, making it resilient, observable, and horizontally scalable.

**Key design goals:**
- **Event-driven** — services communicate asynchronously via Apache Kafka, eliminating tight coupling and enabling each service to scale independently.
- **Observable** — every service is instrumented with distributed tracing (Zipkin) and metrics (Prometheus + Grafana), giving full visibility into system health.
- **Resilient** — an API Gateway acts as the single entry point, and service discovery via Eureka means no service needs to know the physical address of another.

---

## 🏛️ Architecture

```
                          ┌─────────────────────────────────────┐
                          │           Client (HTTP/REST)          │
                          └──────────────────┬──────────────────┘
                                             │
                          ┌──────────────────▼──────────────────┐
                          │         API Gateway (Port 8080)       │
                          │        Spring Cloud Gateway           │
                          └──────────────────┬──────────────────┘
                                             │
               ┌─────────────────────────────┼─────────────────────────────┐
               │                             │                             │
  ┌────────────▼──────────┐   ┌─────────────▼──────────┐   ┌─────────────▼──────────┐
  │    Auth Service        │   │   Inventory Service      │   │    Order Service        │
  │      (Port 8081)       │   │      (Port 8082)         │   │      (Port 8083)        │
  │  JWT · Spring Security │   │  Stock · Warehousing     │   │  Orders · Fulfilment    │
  └────────────┬───────────┘   └─────────────┬───────────┘   └──────────────┬──────────┘
               │                             │                               │
               │               ┌─────────────▼───────────────────────────────┘
               │               │         Apache Kafka (Event Bus)
               │               │   Topics: inventory.updated · order.created
               │               │           order.fulfilled · stock.alert
               │               └───────────────────────────────────┐
               │                                                   │
  ┌────────────▼──────────┐                         ┌─────────────▼──────────┐
  │   PostgreSQL (Auth)    │                         │    PostgreSQL (Orders)   │
  │   Redis (Token Cache)  │                         │    Redis (Stock Cache)   │
  └────────────────────────┘                         └────────────────────────┘

  ┌─────────────────────────────────────────────────────────────────────────────┐
  │                     Infrastructure & Observability                           │
  │    Eureka Server · Zipkin (Tracing) · Prometheus (Metrics) · Grafana (UI)   │
  └─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🧩 Services

| Service | Port | Responsibility |
|---|---|---|
| **`api-gateway`** | `8080` | Single entry point. Routes requests, handles rate limiting and authentication pre-filtering via Spring Cloud Gateway. |
| **`auth-service`** | `8081` | User registration, login, and JWT issuance. Tokens are validated by the gateway on every request. Uses Redis to cache and invalidate tokens. |
| **`inventory-service`** | `8082` | Manages products, stock levels, and warehouse locations. Publishes `inventory.updated` events to Kafka when stock changes. |
| **`order-service`** | `8083` | Creates and tracks orders through their full lifecycle. Consumes `inventory.updated` events to validate stock before confirming orders. Publishes `order.created` events upon success. |
| **`eureka-server`** | `8761` | Service registry. All services register here on startup; no service needs a hardcoded URL of another. |

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.x, Spring Cloud |
| **API Gateway** | Spring Cloud Gateway |
| **Service Discovery** | Netflix Eureka |
| **Messaging** | Apache Kafka |
| **Databases** | PostgreSQL (per service) |
| **Caching** | Redis |
| **Security** | Spring Security, JWT |
| **Distributed Tracing** | Zipkin + Spring Cloud Sleuth |
| **Metrics** | Micrometer + Prometheus |
| **Dashboards** | Grafana |
| **Containerisation** | Docker, Docker Compose |
| **Build Tool** | Apache Maven |
| **API Docs** | Springdoc OpenAPI (Swagger UI) |

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (v24+)
- [Java 17+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/atlas.git
cd atlas
```

### 2. Start the entire platform

A single command boots all services, databases, Kafka, and the observability stack:

```bash
docker-compose up -d
```

> ⏳ First boot takes ~90 seconds while images are pulled and services register with Eureka. You can monitor startup with `docker-compose logs -f`.

### 3. Verify everything is running

```bash
docker-compose ps
```

All services should show `Up (healthy)`.

### Service URLs

| Service | URL |
|---|---|
| API Gateway | http://localhost:8080 |
| Eureka Dashboard | http://localhost:8761 |
| Swagger UI (Inventory) | http://localhost:8082/swagger-ui.html |
| Swagger UI (Orders) | http://localhost:8083/swagger-ui.html |
| Grafana | http://localhost:3000 (admin / admin) |
| Zipkin | http://localhost:9411 |
| Prometheus | http://localhost:9090 |

### Shutting down

```bash
docker-compose down          # Stop containers, keep volumes (data persists)
docker-compose down -v       # Stop containers AND wipe all data
```

---

## 📚 API Documentation

Each service exposes a **Swagger UI** powered by Springdoc OpenAPI. All requests should go through the **API Gateway** on port `8080`.

### Authentication

First, obtain a JWT token:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "username": "admin", "password": "password" }'
```

Use the returned token as a Bearer header on all subsequent requests:

```bash
curl http://localhost:8080/api/inventory \
  -H "Authorization: Bearer <your_token>"
```

### Sample Requests

**Create an inventory item:**
```bash
curl -X POST http://localhost:8080/api/inventory \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "WH-1042",
    "name": "Industrial Valve",
    "quantity": 250,
    "warehouseId": "WH-MUMBAI-01"
  }'
```

**Place an order:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      { "sku": "WH-1042", "quantity": 10 }
    ]
  }'
```

---

## 📊 Observability

Atlas ships with a complete observability stack out of the box.

### Distributed Tracing — Zipkin

Every request that flows through Atlas generates a **trace ID** that follows it across all services. Open [Zipkin](http://localhost:9411) to visualise end-to-end latency and pinpoint failures.

### Metrics & Dashboards — Prometheus + Grafana

Prometheus scrapes metrics from every service. Pre-built Grafana dashboards (located in `/grafana/dashboards`) visualise:

- JVM heap usage per service
- Kafka consumer lag
- HTTP request rates and error rates (4xx / 5xx)
- Order throughput and inventory update frequency

Login to Grafana at http://localhost:3000 with `admin` / `admin`.

---

## 🗺️ Roadmap

- [x] Core microservices (auth, inventory, order)
- [x] Event-driven architecture via Kafka
- [x] Full observability stack (Zipkin, Prometheus, Grafana)
- [ ] Comprehensive unit & integration tests (JUnit 5, Mockito, Testcontainers)
- [ ] Circuit breaker pattern with Resilience4j
- [ ] Global exception handling with standardised error responses
- [ ] GitHub Actions CI pipeline (`mvn clean verify` on every push)
- [ ] Notification service (email / webhook alerts on stock thresholds)

---

## 📄 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.