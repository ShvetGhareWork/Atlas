# 🤖 AI Optimizer Service

`ai-optimizer-service` is a microservice dedicated to real-time supply chain analysis, dynamic routing, and inventory allocation optimization within the **Atlas Supply Chain Platform**. Built using **Spring Boot**, it will act as the brains of the inventory distribution layer.

---

## 📖 Overview

The AI Optimizer Service analyzes historical supply chain patterns, stock consumption rates, and transit data to provide predictive insights. It helps dynamically allocate safety stock levels, recommend optimal shipping warehouses to minimize logistics costs, and foresee stockouts before they happen.

---

## ⚡ Planned & Key Features

- **Predictive Stock Allocation** — Recommends optimal product stock distribution across multiple warehouses based on seasonal demand forecasting.
- **Route & Carrier Optimization** — Calculates the fastest and most cost-effective shipping route/carrier combinations for order fulfillment.
- **Inventory Threshold Optimizer** — Analyzes past order frequency to suggest smart, dynamic reorder thresholds (replacing rigid static limits).
- **Eureka Integration** — Registers dynamically with `discovery-server` to cooperate with `inventory-service` and `order-service`.

---

## ⚙️ Configuration & Ports

- **Port:** `8085` (default dev port)
- **Service Registration Name:** `ai-optimizer-service`

### Core properties in `application.yml`
```yaml
server:
  port: 8085

spring:
  application:
    name: ai-optimizer-service

eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:http://localhost:8761/eureka/}
```

---

## 🚀 Running Locally

### Pre-requisites
- JDK 17
- Maven 3.9+
- Running `discovery-server` (Port 8761)

### Commands
1. Navigate to the directory:
   ```bash
   cd ai-optimizer-service
   ```
2. Build the JAR:
   ```bash
   mvn clean package -DskipTests
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

> [!NOTE]
> This service is under active construction and will soon feature integration with popular deep learning and statistical modeling frameworks (e.g., Python/ONNX runtimes, Or-Tools) to perform complex linear programming and routing optimizations.
