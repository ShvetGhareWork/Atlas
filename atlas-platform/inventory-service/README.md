# 📦 Inventory Service

`inventory-service` is the microservice responsible for catalog, product tracking, stock level management, and warehouse locations in the **Atlas Supply Chain Platform**. Built using **Spring Boot** and **PostgreSQL**, it serves as the authoritative source of truth for stock availability.

---

## 📖 Overview

The Inventory Service exposes REST APIs to create, read, update, and delete products, manage stock counts, and handle warehouse locations. To guarantee high performance, stock queries are cached in **Redis**. In addition, to support asynchronous, event-driven processes, the service publishes real-time stock change events to **Apache Kafka**.

---

## ⚡ Key Features

- **Product Catalog** — Manages products, categories, dimensions, and metadata.
- **Stock Management** — Real-time tracking of physical quantities, reserved quantities, and safety thresholds.
- **Event-Driven Architecture** — Automatically publishes `inventory.updated` and `stock.alert` events to Kafka topics when levels drop or items change, keeping other microservices (like `order-service`) in sync.
- **Warehouse Logistics** — Assigns products to specific warehouses or bin locations.
- **Redis Caching** — Caches high-traffic catalog lookups to minimize database overhead.

---

## ⚙️ Configuration & Ports

- **Port:** `8083`
- **Database:** PostgreSQL (`inventorydb` on port 7779)
- **Message Bus:** Apache Kafka (Topics: `inventory.updated`, `stock.alert`)
- **Caching:** Redis

### Core properties in `application.yml`
```yaml
server:
  port: 8083

spring:
  application:
    name: inventory-service
  datasource:
    url: jdbc:postgresql://localhost:7779/inventorydb
  kafka:
    bootstrap-servers: localhost:9092
```

---

## 📚 API Reference

All client requests should go through the **API Gateway** on port `8080`.

### 1. Retrieve Stock Level
- **URL:** `GET /api/inventory/{sku}`
- **Response:**
  ```json
  {
    "sku": "WH-1042",
    "name": "Industrial Valve",
    "quantity": 250,
    "warehouseId": "WH-MUMBAI-01",
    "status": "IN_STOCK"
  }
  ```

### 2. Create Product/Stock
- **URL:** `POST /api/inventory`
- **Request Body:**
  ```json
  {
    "sku": "WH-1042",
    "name": "Industrial Valve",
    "quantity": 250,
    "warehouseId": "WH-MUMBAI-01"
  }
  ```
- **Response:** `201 Created`

---

## 📣 Event Schema (Kafka)

When stock level changes, a message is published to the `inventory.updated` topic:
```json
{
  "eventId": "de305d54-75b4-431b-adb2-eb6b9e546013",
  "sku": "WH-1042",
  "oldQuantity": 250,
  "newQuantity": 240,
  "updatedAt": "2026-05-25T13:20:00Z"
}
```

---

## 🚀 Running Locally

### Pre-requisites
- JDK 17
- Maven 3.9+
- Running Kafka Broker (Port 9092)
- Running PostgreSQL database (Port 7779)
- Running Redis cache (Port 6379)

### Commands
1. Navigate to the directory:
   ```bash
   cd inventory-service
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
> Access the Swagger documentation for this service at [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) to explore all interactive endpoints and schemas.
