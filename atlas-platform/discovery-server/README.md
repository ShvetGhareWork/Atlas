# 🛰️ Discovery Server (Netflix Eureka)

`discovery-server` is a central service registry built on Netflix Eureka. It serves as the phonebook for the entire Atlas microservices ecosystem, allowing other microservices to find each other dynamically without hardcoded hostnames or ports.

---

## 📖 Overview

In a cloud-native microservices architecture, services spin up, down, and scale horizontally on dynamic IP addresses. Instead of manually updating routing tables, every microservice registers itself with the Discovery Server upon startup, providing its host, port, metadata, and health status.

---

## ⚡ Features

- **Dynamic Service Registry** — Auto-registers microservices as they boot up.
- **Client-Side Load Balancing integration** — Integrates seamlessly with Spring Cloud Gateway and Spring `WebClient` to route requests across healthy service instances.
- **Self-Preservation Mode** — Prevents active service instances from being prematurely evicted during minor network splits.
- **Rich Dashboard UI** — Interactive web dashboard visualizing all registered client instances, system uptime, and memory usage.

---

## ⚙️ Configuration & Ports

- **Port:** `8761`
- **Context Path:** `/`
- **Dashboard URL:** [http://localhost:8761](http://localhost:8761)

### Key properties in `application.yml`
```yaml
server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false # Discovery server doesn't register with itself
    fetchRegistry: false      # Discovery server doesn't pull registries
```

---

## 🚀 Running Locally

### Pre-requisites
- JDK 17
- Maven 3.9+

### Commands
1. Navigate to the directory:
   ```bash
   cd discovery-server
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
> Once started, access the Eureka console at [http://localhost:8761](http://localhost:8761) to monitor registered instances of other services (e.g., `identity-service`, `order-service`, `inventory-service`).
