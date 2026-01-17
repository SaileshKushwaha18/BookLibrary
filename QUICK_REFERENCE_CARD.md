# 🚀 BookLibrary Microservices - Quick Reference Card

**Spring Boot 3.4.0 | JDK 17 | Spring Cloud 2023.0.3 | Production Ready ✅**

---

## 🎯 Service URLs

| Service | Port | URL | Purpose |
|---------|------|-----|---------|
| **API Gateway** | 8080 | http://localhost:8080 | Production entry point |
| **Book Service** | 8081 | http://localhost:8081 | Book management API |
| **Subscription Service** | 8082 | http://localhost:8082 | Subscription management API |
| **Eureka Server** | 8761 | http://localhost:8761 | Service discovery |

---

## 🏃 Quick Start (5 minutes)

### 1. Start All Services (4 terminals)
```bash
# Terminal 1: Eureka Server
cd BookLibraryEurekaServer && mvn spring-boot:run

# Terminal 2: Book Service
cd BookService && mvn spring-boot:run

# Terminal 3: Subscription Service
cd SubscriptionService && mvn spring-boot:run

# Terminal 4: API Gateway
cd ApiGatewayService && mvn spring-boot:run
```

### 2. Verify Services Running
```bash
# All should return "UP" status
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8080/actuator/health

# View registered services
open http://localhost:8761/
```

### 3. Import Postman Collection
```
File → Import → BookLibraryAPIMicroService.postman_collection.json
```

### 4. Run Your First Request
```
Collections → 0. Service Health & Discovery → Book Service Health → Send
```

---

## 📚 Key API Endpoints

### Via API Gateway (Recommended for Production)
```
GET  /book-service/books              # List all books
GET  /book-service/books/{bookId}     # Get specific book
PUT  /book-service/books/{bookId}     # Update book

GET  /subscription-service/subscriptions         # List subscriptions
GET  /subscription-service/subscriptions/{id}    # Get subscription
POST /subscription-service/subscriptions         # Create subscription
```

### Direct Service Access (Development Only)
```
# Book Service (Port 8081)
GET  /books
GET  /books/{bookId}
PUT  /books/{bookId}

# Subscription Service (Port 8082)
GET  /subscriptions
GET  /subscriptions/{id}
POST /subscriptions
```

---

## 🔍 Monitoring Endpoints

### On Each Service (replace {port} with 8081/8082/8080)
```
GET /actuator/health                    # Health status
GET /actuator/metrics                   # Available metrics
GET /actuator/metrics/http.server.requests  # Request metrics
GET /actuator/prometheus                # Prometheus format
GET /actuator/circuitbreaker-events     # Circuit breaker events
GET /v3/api-docs                        # OpenAPI specification
GET /swagger-ui.html                    # Interactive API docs
```

### Service Discovery
```
GET {{eurekaServer}}/                   # Eureka dashboard
```

---

## 💡 Common Tasks

### Create a Subscription
```json
POST /subscription-service/subscriptions
Content-Type: application/json

{
  "subscriberName": "John Doe",
  "dateSubscriber": "2024-01-17",
  "dateReturned": null,
  "bookId": "B1212"
}
```

### Check Book Availability
```
GET /book-service/books/B1212
```

### Update Book Copies
```json
PUT /book-service/books/B1212
Content-Type: application/json

{
  "bookId": "B1212",
  "remainingCopies": 5
}
```

### Check Circuit Breaker Status
```
GET /actuator/circuitbreaker-events
```

---

## 🧪 Testing Quick Guide

### Smoke Test
1. Check all health endpoints (Eureka, Book, Subscription, Gateway)
2. List books via gateway
3. List subscriptions via gateway
4. Create a subscription
5. Check circuit breaker status

### Error Testing
1. GET /books/INVALID → Expect 404
2. POST /subscriptions with invalid data → Expect 400
3. Stop Book Service, test circuit breaker state
4. Restart Book Service, verify recovery

### Performance Check
1. GET /book-service/books with many records
2. Check response compression (Content-Encoding: gzip)
3. Check response time metrics
4. Monitor memory usage

---

## ⚙️ Environment Variables (Postman)

Set these in Postman Environment:
```
baseUrl = http://localhost:8080              (API Gateway)
bookServiceDirect = http://localhost:8081   (Book Service)
subscriptionServiceDirect = http://localhost:8082  (Subscription)
eurekaServer = http://localhost:8761        (Eureka)
```

---

## 🛡️ REST Standards Used

| Aspect | Standard | Example |
|--------|----------|---------|
| **Methods** | GET, POST, PUT | GET /books, POST /subscriptions |
| **Status Codes** | 200, 201, 400, 404 | 201 on successful POST |
| **URLs** | Resource-based | /books/{id}, not /getBooks |
| **Headers** | Content-Type, Accept | application/json |
| **Bodies** | JSON | {"key": "value"} |

---

## 🔄 Microservices Features

| Feature | Implementation | Details |
|---------|----------------|---------|
| **Service Discovery** | Eureka | Services auto-register, lb:// routing |
| **API Gateway** | Spring Cloud Gateway | Reactive, load-balanced routing |
| **Resilience** | Resilience4j | Circuit breaker on service calls |
| **Monitoring** | Actuator + Prometheus | Metrics, health, events |
| **Documentation** | Springdoc OpenAPI 3.0 | Auto-generated from code |
| **Performance** | HTTP compression | gzip on large responses |

---

## 📊 Circuit Breaker Behavior

### States
- **CLOSED** (normal): All requests go to service
- **OPEN** (failure): Service failing, return fallback
- **HALF_OPEN** (recovery): Testing if service recovered

### Configuration
- Failure threshold: 50% of 10 requests
- Recovery timeout: 15 seconds
- Fallback: Returns 0 remaining copies

### Testing
```bash
# Monitor in real-time
curl -s http://localhost:8082/actuator/circuitbreaker-events | jq .

# Trigger by stopping Book Service
kill <book-service-pid>

# Watch circuit transition: CLOSED → OPEN → HALF_OPEN → CLOSED
```

---

## 🚨 Troubleshooting

| Issue | Solution |
|-------|----------|
| **404 Not Found** | Service not registered with Eureka, wait 30 sec |
| **Connection Timeout** | Service not running, check port |
| **Circuit Open** | Service down, wait 15 sec recovery timeout |
| **No Compression** | Response < 1KB, needs more data |

---

## 📖 Documentation

| Document | Purpose |
|----------|---------|
| [README.md](README.md) | Architecture, setup |
| [UPGRADE_GUIDE.md](UPGRADE_GUIDE.md) | Migration details |
| [POSTMAN_COLLECTION_GUIDE.md](POSTMAN_COLLECTION_GUIDE.md) | API testing guide |
| [PRODUCTION_READINESS_CHECKLIST.md](PRODUCTION_READINESS_CHECKLIST.md) | Deployment checklist |
| [PROJECT_ANALYSIS_AND_VERIFICATION.md](PROJECT_ANALYSIS_AND_VERIFICATION.md) | Technical deep-dive |

---

## 🔗 Useful Links

- **Eureka Dashboard**: http://localhost:8761/
- **Book Service Docs**: http://localhost:8081/swagger-ui.html
- **Subscription Docs**: http://localhost:8082/swagger-ui.html
- **OpenAPI JSON (Book)**: http://localhost:8081/v3/api-docs
- **OpenAPI JSON (Subscription)**: http://localhost:8082/v3/api-docs

---

## ✅ Production Checklist

- [x] All services upgraded to Spring Boot 3.4.0
- [x] JDK 17 configured
- [x] Jakarta imports migrated
- [x] Circuit breaker implemented
- [x] Monitoring configured
- [x] REST standards followed
- [x] API documentation generated
- [x] Postman collection updated

---

**Status**: 🟢 Production Ready  
**Last Updated**: January 17, 2026  
**Version**: 3.4.0  

Keep this card handy for quick reference during development and testing!
