# 🚀 Spring Boot Microservices Observability Demo

A complete hands-on observability learning project using Spring Boot microservices with:

* Distributed Logging
* Metrics Monitoring
* Alerting
* Slack Notifications
* Centralized Visualization
* Correlation IDs
* Prometheus
* Grafana
* Alertmanager
* Docker Compose

---

# 📚 Table of Contents

* [Architecture Overview](#-architecture-overview)
* [Microservices](#-microservices)
* [Project Structure](#-project-structure)
* [Communication Flow](#-communication-flow)
* [Observability Overview](#-observability-overview)
* [LMT Model](#-lmt-model)
* [Prometheus](#-prometheus)
* [Grafana](#-grafana)
* [Alertmanager](#-alertmanager)
* [Slack Integration](#-slack-integration)
* [Docker Setup](#-docker-setup)
* [Metrics](#-metrics)
* [Failure Scenarios](#-failure-scenarios)
* [Correlation IDs](#-correlation-ids)
* [Future Improvements](#-future-improvements)
* [Useful Commands](#-useful-commands)

---

# 🏗 Architecture Overview

```text
                +----------------+
                |    Grafana     |
                +----------------+
                  /      |      \
                 /       |       \
                v        v        v

         Prometheus    Loki     Tempo
          (Metrics)    (Logs)   (Traces)
                |
                v
          Alertmanager
                |
                v
              Slack


      +--------------------------------+
      | Spring Boot Microservices      |
      +--------------------------------+
         |          |           |
         v          v           v
     user-service product-service order-service
```

---

# 🔧 Microservices

## 1. user-service

Handles:

* User APIs
* User data
* User retrieval

### APIs

```http
GET /users/{id}
POST /users
GET /users
```

### Port

```text
8081
```

---

## 2. product-service

Handles:

* Product APIs
* Product inventory
* Product retrieval

### APIs

```http
GET /products/{id}
POST /products
GET /products
```

### Port

```text
8082
```

---

## 3. order-service

Handles:

* Order creation
* Communication with downstream services
* Metrics generation
* Failure simulation

### APIs

```http
POST /orders
GET /orders/{id}
```

### Port

```text
8083
```

---

# 📁 Project Structure

```text
observability-demo/
│
├── user-service/
│    ├── src/
│    ├── pom.xml
│
├── product-service/
│    ├── src/
│    ├── pom.xml
│
├── order-service/
│    ├── src/
│    ├── pom.xml
│
├── monitoring/
│    ├── prometheus.yml
│    ├── alert-rules.yml
│    └── alertmanager.yml
│
└── docker-compose.yml
```

---

# 🔄 Communication Flow

## Order Creation Flow

```text
Client
   |
   v
order-service
   |
   +-------> user-service
   |
   +-------> product-service
```

---

## Detailed Request Flow

```text
POST /orders
      |
      v
order-service
      |
      |---- Fetch User
      v
user-service

      |
      |---- Fetch Product
      v
product-service

      |
      |---- Save Order
      v
H2 Database
```

---

# 👀 Observability Overview

Observability helps understand:

* What happened?
* Why did it happen?
* Which service failed?
* Where latency occurred?
* How often failures occur?

---

# 📊 LMT Model

## Logs, Metrics, Traces

| Pillar  | Meaning              | Answers             |
| ------- | -------------------- | ------------------- |
| Logs    | Detailed events      | What happened?      |
| Metrics | Numeric measurements | How much/how often? |
| Traces  | Request journey      | Where did time go?  |

---

# 📝 Logs

Logs provide detailed event information.

### Example

```text
[a6a2f3c7] Creating order for userId=1
[a6a2f3c7] Product service failed
```

### Current Logging Features

* Correlation IDs
* Distributed request tracking
* MDC logging
* Structured logging

---

# 📈 Metrics

Metrics provide numeric measurements.

### Example Metrics

```text
orders_created_total
orders_creation_time_seconds
orders_errors_total
```

### Metrics Generated

* Request count
* Latency
* Error count
* JVM metrics
* HTTP metrics

---

# 🔍 Traces

Tracing tracks requests across microservices.

### Example Trace

```text
Order Service
   |
   +---- User Service (40ms)
   |
   +---- Product Service (3s)
```

### Tracing Tools

* OpenTelemetry
* Tempo
* Zipkin
* Jaeger

---

# 📦 Prometheus

Prometheus is a:

```text
Time-series metrics database
```

Used for:

* Metrics collection
* Alerting
* Monitoring
* Time-series storage

---

## Prometheus Responsibilities

* Scrape metrics
* Store time-series data
* Execute alert rules
* Query metrics using PromQL

---

## Prometheus Endpoints

### Prometheus UI

```text
http://localhost:9090
```

### Targets

```text
http://localhost:9090/targets
```

### Alerts

```text
http://localhost:9090/alerts
```

---

## Prometheus Important Queries

### Total Orders

```promql
orders_created_total
```

### Error Count

```promql
orders_errors_total
```

### Latency

```promql
orders_creation_time_seconds_max
```

### JVM Memory

```promql
jvm_memory_used_bytes
```

---

# 📊 Grafana

Grafana is used for:

* Dashboards
* Visualization
* Graphs
* Monitoring panels

---

## Grafana URL

```text
http://localhost:3000
```

---

## Default Credentials

```text
Username: admin
Password: admin
```

---

## Grafana Data Source

### Prometheus URL

```text
http://prometheus:9090
```

---

## Recommended Dashboard Panels

| Panel          | Query                              |
| -------------- | ---------------------------------- |
| Orders Created | `orders_created_total`             |
| Order Errors   | `orders_errors_total`              |
| Max Latency    | `orders_creation_time_seconds_max` |
| JVM Memory     | `jvm_memory_used_bytes`            |
| Live Threads   | `jvm_threads_live_threads`         |

---

# 🚨 Alertmanager

Alertmanager handles:

* Alert routing
* Notifications
* Grouping
* Deduplication
* Incident notifications

---

## Alertmanager URL

```text
http://localhost:9093
```

---

## Example Alerts

### High Latency

```text
Order creation latency > 3s
```

### Service Down

```text
product-service unavailable
```

### Too Many Failures

```text
High order failure rate
```

---

# 💬 Slack Integration

Slack notifications are configured through:

```text
Alertmanager -> Slack Webhook
```

---

## Slack Flow

```text
Prometheus Alert
      |
      v
Alertmanager
      |
      v
Slack Notification
```

---

## Slack App Setup

### Steps

1. Create Slack App
2. Enable Incoming Webhooks
3. Add Webhook to Workspace
4. Copy Webhook URL
5. Configure Alertmanager

---

## Slack Alert Example

```text
Product service down
Severity: critical
```

---

# 🐳 Docker Setup

Monitoring tools run using Docker Compose.

---

## Docker Compose Start

```bash
docker compose up -d
```

---

## Stop Containers

```bash
docker compose down
```

---

## Restart Containers

```bash
docker compose restart
```

---

## View Containers

```bash
docker ps
```

---

## View Logs

### Prometheus Logs

```bash
docker logs prometheus
```

### Alertmanager Logs

```bash
docker logs alertmanager
```

### Grafana Logs

```bash
docker logs grafana
```

---

# 📄 docker-compose.yml

```yaml
services:

  prometheus:
    image: prom/prometheus
    container_name: prometheus

    ports:
      - "9090:9090"

    volumes:
      - ./monitoring/prometheus.yml:/etc/prometheus/prometheus.yml
      - ./monitoring/alert-rules.yml:/etc/prometheus/alert-rules.yml

  grafana:
    image: grafana/grafana
    container_name: grafana

    ports:
      - "3000:3000"

  alertmanager:
    image: prom/alertmanager
    container_name: alertmanager

    ports:
      - "9093:9093"

    volumes:
      - ./monitoring/alertmanager.yml:/etc/alertmanager/alertmanager.yml
```

---

# 📊 Metrics Examples

## Successful Orders

```java
meterRegistry.counter("orders.created").increment();
```

---

## Failed Orders

```java
meterRegistry.counter("orders.failed",
        "reason", "random_failure")
        .increment();
```

---

## Downstream Errors

```java
meterRegistry.counter("downstream.errors",
        "service", "product-service")
        .increment();
```

---

## Latency Metrics

```java
Timer.Sample sample = Timer.start(meterRegistry);
```

---

# ❌ Failure Scenarios Implemented

| Scenario             | Purpose                  |
| -------------------- | ------------------------ |
| Random order failure | Error metrics            |
| Product service down | Downstream failure       |
| Artificial latency   | Slow API simulation      |
| Insufficient stock   | Business failure metrics |

---

# 🔗 Correlation IDs

Correlation IDs help track requests across services.

### Example

```text
[a6a2f3c7]
```

Shared across:

* order-service
* user-service
* product-service

---

# 📌 Important Endpoints

## order-service

```text
http://localhost:8083/actuator/prometheus
```

---

## user-service

```text
http://localhost:8081/actuator/prometheus
```

---

## product-service

```text
http://localhost:8082/actuator/prometheus
```

---

# 🧠 Important Observability Concepts

| Concept             | Description                |
| ------------------- | -------------------------- |
| Correlation IDs     | Track distributed requests |
| Metrics             | Numeric monitoring         |
| Logs                | Detailed event records     |
| Traces              | Request journey            |
| Alerting            | Incident notifications     |
| Distributed tracing | Cross-service visibility   |
| Time-series DB      | Metrics storage            |

---

# 🚀 Future Improvements

## Recommended Next Steps

### Logging

* Loki
* Fluent Bit
* Structured JSON logs

---

### Tracing

* OpenTelemetry
* Tempo
* Zipkin

---

### Infrastructure

* Dockerize services
* Kubernetes deployment
* Helm charts
* CI/CD integration

---

### Advanced Monitoring

* SLOs
* SLIs
* Error budgets
* Distributed tracing correlation

---

# 🛠 Useful Commands

## Start Monitoring Stack

```bash
docker compose up -d
```

---

## Stop Monitoring Stack

```bash
docker compose down
```

---

## Restart Prometheus

```bash
docker compose restart prometheus
```

---

## Restart Alertmanager

```bash
docker compose restart alertmanager
```

---

## Check Prometheus Targets

```text
http://localhost:9090/targets
```

---

## Check Alerts

```text
http://localhost:9090/alerts
```

---

## Check Alertmanager

```text
http://localhost:9093
```

---

## Open Grafana

```text
http://localhost:3000
```

---

# 🎯 Final Learning Outcome

This project demonstrates:

✅ Microservices observability

✅ Metrics collection

✅ Distributed logging

✅ Correlation IDs

✅ Prometheus monitoring

✅ Grafana dashboards

✅ Alertmanager routing

✅ Slack notifications

✅ Failure simulation

✅ Latency monitoring

✅ Production-style monitoring architecture

---

# 📖 Recommended Future Stack

```text
Prometheus  -> Metrics
Loki        -> Logs
Tempo       -> Traces
Grafana     -> Visualization
Alertmanager -> Alert routing
OpenTelemetry -> Instrumentation
```

---

# 🙌 Conclusion

This project provides a complete hands-on introduction to modern observability using:

* Spring Boot
* Prometheus
* Grafana
* Alertmanager
* Slack
* Docker Compose

and demonstrates real-world observability concepts used in modern distributed systems.
