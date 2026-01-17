# Book Library Microservices - Spring Boot 3 & JDK 17

A production-grade Book Library microservices application built with Spring Boot 3.4.0, JDK 17, and modern cloud-native technologies.

## Technology Stack

- **Runtime**: JDK 17
- **Framework**: Spring Boot 3.4.0
- **Cloud**: Spring Cloud 2023.0.3
- **Service Discovery**: Netflix Eureka Server
- **API Gateway**: Spring Cloud Gateway (replaces deprecated Zuul)
- **Circuit Breaking**: Resilience4j (replaces deprecated Hystrix)
- **Load Balancing**: Spring Cloud LoadBalancer
- **API Documentation**: Springdoc OpenAPI 3.0 (Swagger UI)
- **Database**: H2 (embedded)
- **Build**: Maven
- **Monitoring**: Spring Boot Actuator with Prometheus metrics

## Project Structure

```
BookLibrary/
├── BookLibraryEurekaServer/      # Service Discovery Server
├── ApiGatewayService/             # API Gateway with Spring Cloud Gateway
├── BookService/                   # Book Management Microservice
├── SubscriptionService/           # Subscription Management Microservice
├── UPGRADE_GUIDE.md              # Detailed upgrade documentation
└── BookLibraryAPIMicroService.postman_collection.json
```

## Service Architecture

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ (HTTP)
       ▼
┌────────────────────────┐
│   API Gateway (8080)   │  ← Spring Cloud Gateway
├────────────────────────┤
│  Route to:             │
│  - /book-service/**    │
│  - /subscription-**    │
└────────┬───────────────┘
         │
    ┌────┴────┐
    │          │
    ▼          ▼
┌────────────────────┐  ┌──────────────────────┐
│  Book Service      │  │ Subscription Service │
│  (8081)            │  │ (8082)               │
│  - Get Books       │  │ - Manage Subscriptions│
│  - Update Copies   │  │ - Check Availability │
└────────┬───────────┘  └──────────┬───────────┘
         │                         │
         └────────┬────────────────┘
                  │ (Eureka Client)
                  ▼
          ┌──────────────────┐
          │  Eureka Server   │
          │  (8761)          │
          │  Registry        │
          └──────────────────┘
```

## Quick Start

### Prerequisites
- JDK 17 or higher
- Maven 3.8.0 or higher

### Installation & Running

1. **Start Eureka Server** (Service Discovery)
```bash
cd BookLibraryEurekaServer
mvn clean spring-boot:run
# Eureka dashboard: http://localhost:8761
```

2. **Start Book Service**
```bash
cd BookService
mvn clean spring-boot:run
# Health: http://localhost:8081/actuator/health
# Swagger UI: http://localhost:8081/swagger-ui.html
# API Docs: http://localhost:8081/v3/api-docs
```

3. **Start Subscription Service**
```bash
cd SubscriptionService
mvn clean spring-boot:run
# Health: http://localhost:8082/actuator/health
# Swagger UI: http://localhost:8082/swagger-ui.html
# API Docs: http://localhost:8082/v3/api-docs
```

4. **Start API Gateway**
```bash
cd ApiGatewayService
mvn clean spring-boot:run
# Gateway: http://localhost:8080
```

## API Endpoints

### Through API Gateway

**Get all books**:
```bash
curl http://localhost:8080/book-service/books
```

**Get book by ID**:
```bash
curl http://localhost:8080/book-service/books/B1212
```

**Get all subscriptions**:
```bash
curl http://localhost:8080/subscription-service/subscriptions
```

**Create subscription**:
```bash
curl -X POST http://localhost:8080/subscription-service/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "subscriberName": "John Doe",
    "dateSubscriber": "2024-01-17",
    "bookId": "B1212"
  }'
```

### Direct Service Access

**Book Service** (Port 8081):
- GET `/books` - Get all books
- GET `/books/{bookId}` - Get book by ID
- PUT `/books/{bookId}` - Update book copies

**Subscription Service** (Port 8082):
- GET `/subscriptions` - Get all subscriptions
- POST `/subscriptions` - Create subscription

## Monitoring & Observability

### Health Checks
```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
```

### Application Metrics (Prometheus format)
```bash
curl http://localhost:8081/actuator/metrics
curl http://localhost:8081/actuator/prometheus
```

### Circuit Breaker Status
```bash
curl http://localhost:8082/actuator/circuitbreakers
curl http://localhost:8082/actuator/circuitbreaker-events
```

### Service Registry
```
http://localhost:8761 (Eureka Dashboard)
```

## API Documentation

### Swagger UI
- Book Service: http://localhost:8081/swagger-ui.html
- Subscription Service: http://localhost:8082/swagger-ui.html

### OpenAPI Specification
- Book Service: http://localhost:8081/v3/api-docs
- Subscription Service: http://localhost:8082/v3/api-docs

## Configuration Files

### Eureka Server (`BookLibraryEurekaServer/src/main/resources/application.yml`)
- Port: 8761
- Service registry for service discovery
- Self-preservation mode enabled

### Book Service (`BookService/src/main/resources/application.yml`)
- Port: 8081
- JPA with H2 database
- HTTP compression enabled
- HikariCP connection pooling (max 10, min 5)

### Subscription Service (`SubscriptionService/src/main/resources/application.yml`)
- Port: 8082
- Resilience4j circuit breaker configuration
- Slide window: 10 requests
- Failure rate threshold: 50%
- Circuit breaker timeout: 15 seconds

### API Gateway (`ApiGatewayService/src/main/resources/application.yml`)
- Port: 8080
- Spring Cloud Gateway routes
- Eureka client for service discovery
- Request header injection

## Recent Upgrades (Spring Boot 2 → 3)

For detailed information about upgrades and migration steps, see [UPGRADE_GUIDE.md](./UPGRADE_GUIDE.md)

**Key Changes**:
1. ✅ Spring Boot 2.3.2 → 3.4.0
2. ✅ JDK 8 → JDK 17
3. ✅ Zuul → Spring Cloud Gateway
4. ✅ Hystrix → Resilience4j
5. ✅ Ribbon → Spring Cloud LoadBalancer
6. ✅ SpringFox Swagger 2 → Springdoc OpenAPI 3
7. ✅ `javax.*` → `jakarta.*` imports
8. ✅ Production monitoring & actuator endpoints

## Testing with Postman

Import the provided Postman collection:
```
BookLibraryAPIMicroService.postman_collection.json
```

This includes pre-configured requests for:
- Getting books through gateway and direct service
- Getting subscriptions
- Creating subscriptions
- Testing end-to-end flow

## Performance Features

- **Non-blocking Gateway**: Reactive Spring Cloud Gateway handles high concurrency
- **Circuit Breaking**: Prevents cascading failures with Resilience4j
- **HTTP Compression**: Reduces network traffic by 70-90%
- **Connection Pooling**: HikariCP for optimal database performance
- **Metrics**: Prometheus-compatible metrics for monitoring

## Troubleshooting

### Services not discovered by Eureka
- Check Eureka dashboard at http://localhost:8761
- Verify all services are started
- Check logs for connection errors

### Circuit breaker open for Book Service
- Check if Book Service is running
- View circuit breaker status: `/actuator/circuitbreaker-events`
- Wait for recovery time (default: 15 seconds)

### Swagger UI not loading
- Ensure service is running
- Check for CORS issues in browser console
- Try accessing `/v3/api-docs` directly

### Database errors
- Check H2 database logs
- Verify JPA entities are properly annotated with `@Entity`
- Check for migration issues

## Environment Variables

Set optional environment variables to customize:
```bash
PORT=8761                    # Eureka Server port
EUREKA_URL=http://host:8761 # Custom Eureka URL
JAVA_OPTS="-Xmx512m"       # JVM memory settings
```

## Building with Docker

```dockerfile
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
mvn clean package -DskipTests
docker build -t book-service .
docker run -p 8081:8081 book-service
```

## Production Deployment Checklist

- [ ] JDK 17+ installed on servers
- [ ] All Spring Boot services configured for environment
- [ ] Eureka Server set up and running
- [ ] API Gateway configured with correct service URIs
- [ ] Database backups configured
- [ ] Monitoring and alerting set up
- [ ] Log aggregation configured
- [ ] Security certificates installed (HTTPS)
- [ ] Rate limiting configured
- [ ] Load balancer configured before gateway

## License

Apache License 2.0

## Contributing

Please see CONTRIBUTING.md for guidelines

## Support

For issues and questions:
1. Check Eureka dashboard: http://localhost:8761
2. Review service logs
3. Check circuit breaker status
4. Verify all services are registered

