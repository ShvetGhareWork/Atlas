# 🛒 Order Service

`order-service` is the microservice responsible for managing orders, transaction processing, and order fulfilment workflows in the **Atlas Supply Chain Platform**. Built using **Spring Boot**, **PostgreSQL**, and **Kafka**, it drives the core business transaction layer.

---

## 📖 Overview

The Order Service handles the complete lifecycle of customer orders. To place an order, the service performs stock verification, checks eligibility criteria, and records the order securely in PostgreSQL. It is built in a highly distributed, event-driven pattern, communicating asynchronously with other services via **Apache Kafka** to update stock levels and trigger packaging workflows.

---

## ⚡ Key Features

- **Order Management** — Exposes REST endpoints to create, track, and cancel orders.
- **Asynchronous Validation** — Consumes `inventory.updated` messages to validate physical stock before allowing checkout.
- **Transactional Consistency** — Coordinates local database writes and reliable event emission using transactional patterns.
- **Event-Driven Workflows** — Publishes `order.created` and `order.fulfilled` to Kafka to initiate downstream shipping or dispatching actions.
- **Authentication Protected** — Requests are intercepted and verified via the API Gateway using JWT.

---

## ⚙️ Configuration & Ports

- **Port:** `8081`
- **Database:** PostgreSQL (`order_db` on port 7777)
- **Message Bus:** Apache Kafka (Topics: Consumes `inventory.updated`, Publishes `order.created`, `order.fulfilled`)

### Core properties in `application.yml`
```yaml
server:
  port: 8081

spring:
  application:
    name: order-service
  datasource:
    url: jdbc:postgresql://localhost:7777/order_db
  kafka:
    bootstrap-servers: localhost:9092
```

---

## 📚 API Reference

All protected requests must go through the **API Gateway** on port `8080` with a valid `Authorization: Bearer <token>` header.

### 1. Place an Order
- **URL:** `POST /api/orders`
- **Request Body:**
  ```json
  {
    "items": [
      {
        "sku": "WH-1042",
        "quantity": 5
      }
    ]
  }
  ```
- **Response:**
  ```json
  {
    "orderId": "6f8c7b8c-57c2-4751-ac4d-8b01a61c3bc2",
    "status": "CREATED",
    "totalAmount": 1250.00,
    "createdAt": "2026-05-25T13:20:15Z"
  }
  ```

### 2. View Order History
- **URL:** `GET /api/orders`
- **Response:**
  ```json
  [
    {
      "orderId": "6f8c7b8c-57c2-4751-ac4d-8b01a61c3bc2",
      "status": "CREATED",
      "totalAmount": 1250.00
    }
  ]
  ```

---

## 🚀 Running Locally

### Pre-requisites
- JDK 17
- Maven 3.9+
- Running Kafka Broker (Port 9092)
- Running PostgreSQL database (Port 7777)
- Running API Gateway and Identity Service for auth context

### Commands
1. Navigate to the directory:
   ```bash
   cd order-service
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
> Access the Swagger documentation for this service at [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) to explore all interactive endpoints and schemas.
