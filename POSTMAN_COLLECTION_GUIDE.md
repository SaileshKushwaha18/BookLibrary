# BookLibrary Microservices - Postman Collection Guide

**Version**: 1.0 | **Updated**: January 17, 2026 | **Spring Boot 3.4.0 | JDK 17**

---

## 📋 Overview

The updated Postman collection provides comprehensive API testing for BookLibrary microservices with:

✅ **REST Standards Compliance**
✅ **Microservices Best Practices**
✅ **Production-Grade Endpoints**
✅ **Error Handling Testing**
✅ **Circuit Breaker Testing**
✅ **Monitoring & Observability**
✅ **OpenAPI 3.0 Integration**

---

## 🚀 Quick Start

### 1. Import Collection
1. Open Postman
2. Click **Import** button
3. Select `BookLibraryAPIMicroService.postman_collection.json`
4. Click **Import**

### 2. Configure Environment Variables
The collection uses predefined variables:
- `baseUrl` → http://localhost:8080 (API Gateway)
- `bookServiceDirect` → http://localhost:8081 (Book Service)
- `subscriptionServiceDirect` → http://localhost:8082 (Subscription Service)
- `eurekaServer` → http://localhost:8761 (Eureka Registry)

To modify:
1. Click **Environment** dropdown in top right
2. Select or create environment
3. Edit variable values as needed

### 3. Start Services
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

### 4. Run Requests
- Click any request in the collection
- Review the request details
- Click **Send**
- Check the **Response** tab

---

## 📑 Collection Structure

### **0. Service Health & Discovery** (Foundation)
Verify all services are running before testing APIs.

**Requests**:
- `Eureka Dashboard` - View registered services
- `Book Service Health` - Health check endpoint
- `Subscription Service Health` - Health check endpoint
- `API Gateway Health` - Health check endpoint
- `Book Service Metrics` - Prometheus metrics
- `Subscription Service Circuit Breaker Status` - Circuit breaker state

**REST Standards Implemented**:
- ✅ GET for health checks (safe, idempotent)
- ✅ 200 OK response for healthy services
- ✅ Application/json content type

---

### **1. Book Service API** (Port 8081)
Direct access to Book Service endpoints, bypassing API Gateway.

#### Requests

**GET /books**
```
Method: GET
URL: {{bookServiceDirect}}/books
Headers: Accept: application/json
Response: 200 OK - List[Book]
Description: Retrieve all books
```

**GET /books/{bookId}**
```
Method: GET
URL: {{bookServiceDirect}}/books/B1212
Path Parameter: bookId = B1212
Response: 200 OK - Book | 404 NOT FOUND
Description: Get specific book by ID
```

**PUT /books/{bookId}**
```
Method: PUT
URL: {{bookServiceDirect}}/books/B1212
Headers: Content-Type: application/json
Body: {
  "bookId": "B1212",
  "remainingCopies": 5
}
Response: 200 OK - Updated Book | 404 NOT FOUND
Description: Update book copies available
```

**REST Standards Implemented**:
- ✅ GET for retrieval (safe, idempotent)
- ✅ PUT for complete updates (idempotent)
- ✅ Consistent resource URLs: /books/{id}
- ✅ Proper HTTP status codes (200, 404)
- ✅ JSON request/response bodies
- ✅ Resource-oriented URLs (nouns, not verbs)

**Microservices Standards**:
- ✅ Single Responsibility: Book management only
- ✅ API versioning ready (/v1/books)
- ✅ Health endpoint: /actuator/health
- ✅ Metrics exposed: /actuator/prometheus
- ✅ OpenAPI documentation: /v3/api-docs

---

### **2. Subscription Service API** (Port 8082)
Subscription management with Resilience4j circuit breaker protection.

#### Requests

**GET /subscriptions**
```
Method: GET
URL: {{subscriptionServiceDirect}}/subscriptions
Headers: Accept: application/json
Response: 200 OK - List[Subscription]
Note: Internally calls Book Service via circuit breaker
```

**GET /subscriptions/{id}**
```
Method: GET
URL: {{subscriptionServiceDirect}}/subscriptions/1
Path Parameter: id = 1
Response: 200 OK - Subscription | 404 NOT FOUND
```

**POST /subscriptions**
```
Method: POST
URL: {{subscriptionServiceDirect}}/subscriptions
Headers: 
  - Content-Type: application/json
  - Accept: application/json
Body: {
  "subscriberName": "John Doe",
  "dateSubscriber": "2024-01-17",
  "dateReturned": null,
  "bookId": "B1212"
}
Response: 201 CREATED - Subscription | 400 BAD REQUEST
Features:
  - Auto-generated ID (GenerationType.IDENTITY)
  - Validation on all fields
  - Circuit breaker protection on Book Service call
```

**REST Standards Implemented**:
- ✅ GET for safe retrieval
- ✅ POST for resource creation (returns 201 CREATED)
- ✅ Proper status codes (200, 201, 400, 404)
- ✅ Resource representation in response body
- ✅ Consistent URL patterns
- ✅ Content negotiation (Accept headers)

**Microservices Standards**:
- ✅ Service-to-service via RestTemplate
- ✅ Circuit breaker pattern (Resilience4j)
- ✅ Service discovery (Eureka)
- ✅ Timeout handling (5s connect, 10s read)
- ✅ Fallback on service failure
- ✅ Event tracking on circuit breaker

---

### **3. API Gateway Routes** (Port 8080)
Production routes through Spring Cloud Gateway.

#### Requests

**GET /book-service/books**
```
Method: GET
URL: {{baseUrl}}/book-service/books
Gateway Processing:
  1. Receives request on /book-service/books
  2. Strips /book-service prefix → /books
  3. Routes to lb://book-service (service discovery)
  4. Load balances to available instances
  5. Adds custom header: X-Gateway-Route=book-service
```

**GET /subscription-service/subscriptions**
```
Method: GET
URL: {{baseUrl}}/subscription-service/subscriptions
Gateway Processing:
  1. Receives request on /subscription-service/subscriptions
  2. Strips /subscription-service prefix → /subscriptions
  3. Routes to lb://subscription-service
  4. Adds custom header: X-Gateway-Route=subscription-service
```

**POST /subscription-service/subscriptions**
```
Method: POST
URL: {{baseUrl}}/subscription-service/subscriptions
Gateway Processing:
  - Forwards POST request to backend service
  - Preserves request body (JSON)
  - Adds X-Gateway-Route header
  - Returns response from backend
```

**Microservices Standards Implemented**:
- ✅ Single entry point (API Gateway pattern)
- ✅ Request routing based on path
- ✅ Load balancing via service discovery
- ✅ Request header injection (cross-cutting concerns)
- ✅ Prefix stripping (clean backend URLs)
- ✅ Non-blocking I/O (reactive streams)
- ✅ Service discovery integration (Eureka)

**Spring Cloud Gateway Features**:
- ✅ Reactive processing (WebFlux)
- ✅ Fluent route builder API
- ✅ Multiple filter types (stripPrefix, addRequestHeader)
- ✅ Predicate support (path matching)
- ✅ Service discovery routing (lb:// protocol)

---

### **4. Error Scenarios & Circuit Breaker**
Testing resilience and fault tolerance.

#### Requests

**404 Error Handling**
```
Test: Get non-existent book
GET /books/INVALID123
Expected: 404 NOT FOUND
Body: Error details with message
REST Standard: Proper error response
```

**400 Error Handling (Validation)**
```
Test: Invalid request body
POST /subscriptions with incomplete JSON
Body missing: dateSubscriber, bookId
Expected: 400 BAD REQUEST
Response: Validation error details
REST Standard: Proper error status and message
```

**Circuit Breaker Testing**
```
Scenario: Service failure recovery
1. Stop Book Service (kill terminal with port 8081)
2. Make 5+ requests to GET /subscriptions
3. Circuit breaker opens (state: OPEN)
4. Subsequent requests return fallback (copies = 0)
5. Wait 15 seconds
6. Circuit breaker transitions to HALF_OPEN
7. Single request attempts to validate service
8. If successful: circuit closes
9. If failed: reopens

Configuration:
- Sliding Window: 10 requests
- Minimum Calls: 5
- Failure Threshold: 50%
- Recovery Timeout: 15 seconds
```

**Microservices Resilience Standards**:
- ✅ Circuit breaker pattern
- ✅ Graceful degradation (fallback)
- ✅ Health checks on backend
- ✅ Timeout management
- ✅ Error monitoring and events

---

### **5. End-to-End Integration Tests**
Complete workflows spanning multiple services.

#### Test Scenarios

**Scenario 1: Create Subscription & Check Availability**
```
1. Client sends: POST /subscriptions
   Request: {subscriberName, dateSubscriber, bookId}
2. Subscription Service receives request
3. Validates request data
4. Calls Book Service to check availability
   - Protected by Resilience4j circuit breaker
   - Timeout: 5s connect, 10s read
   - If timeout/error: fallback returns 0 copies
5. Creates subscription in database
6. Returns 201 CREATED with subscription ID

Observables:
- Circuit breaker state (check actuator)
- Response time with compression
- Service-to-service communication latency
- Error handling if Book Service is down
```

**Scenario 2: List Books Then Subscribe**
```
1. Client sends: GET /books (via gateway)
2. Gateway routes to Book Service
   - Request header: X-Gateway-Route=book-service
   - Load balancing via service discovery
3. Response compressed (gzip)
4. Client reviews book list
5. Client sends: POST /subscriptions with selected bookId
6. Same flow as Scenario 1

Observables:
- Gateway request routing
- Header injection working
- HTTP compression effectiveness
- Load balancing across instances
```

---

### **6. Monitoring & Performance**
Production monitoring endpoints.

#### Actuator Endpoints

**Health Check**
```
GET /actuator/health
Response: {
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "circuitBreaker": {"status": "UP"},
    "diskSpace": {"status": "UP"}
  }
}
```

**Metrics**
```
GET /actuator/metrics
Lists all available metrics:
- jvm.* (memory, threads, GC)
- process.* (CPU, files)
- system.* (load average)
- http.server.requests
- logback (logging)
- hikaricp (database pooling)
- resilience4j (circuit breaker)
```

**HTTP Request Metrics**
```
GET /actuator/metrics/http.server.requests
Shows:
- Count of requests
- Response times (p50, p95, p99, max)
- Status codes distribution
- By endpoint and method
```

**Prometheus Metrics**
```
GET /actuator/prometheus
Metrics in Prometheus scrape format
Use for Grafana dashboards
Includes all application and JVM metrics
```

**Circuit Breaker Events**
```
GET /actuator/circuitbreaker-events
Shows:
- Event type (state change, success, error)
- Timestamp
- Circuit breaker name
- Error details
```

---

### **7. API Documentation**
OpenAPI 3.0 specifications and interactive docs.

#### Swagger UI
```
GET /swagger-ui.html
Interactive documentation:
- Try-it-out capability
- Request/response schemas
- Status codes documented
- Parameter descriptions
```

#### OpenAPI JSON
```
GET /v3/api-docs
Machine-readable OpenAPI 3.0 spec
Use for:
- Code generation (OpenAPI generator)
- API documentation tools
- Testing framework integration
- API gateway configurations
```

---

## ✅ REST Standards Compliance

### Request Methods Used

| Method | Usage | Idempotent | Safe |
|--------|-------|-----------|------|
| **GET** | Retrieve resources | ✅ Yes | ✅ Yes |
| **POST** | Create resources | ❌ No | ❌ No |
| **PUT** | Update resources (full) | ✅ Yes | ❌ No |
| **DELETE** | Delete resources | ✅ Yes | ❌ No |

### Status Codes Used

| Code | Meaning | Usage |
|------|---------|-------|
| **200** | OK | Successful GET, PUT |
| **201** | Created | Successful POST |
| **400** | Bad Request | Invalid request data |
| **404** | Not Found | Resource doesn't exist |
| **500** | Server Error | Server-side exception |

### Headers Used

| Header | Purpose | Example |
|--------|---------|---------|
| **Content-Type** | Request body format | application/json |
| **Accept** | Response format preference | application/json |
| **X-Gateway-Route** | Custom routing header | book-service |

### URL Naming Conventions

✅ **Followed**:
- Resource-oriented URLs (nouns): `/books`, `/subscriptions`
- Hierarchy: `/books/{id}`
- No verbs in URLs: Not `/getBooks`, `/createSubscription`
- Lowercase paths
- Consistent naming across services

✅ **Avoided**:
- Action-based URLs
- RPC-style URLs
- Inconsistent naming patterns

---

## 📋 Microservices Standards

### Service Isolation
- ✅ Each service owns its database (H2 in-memory)
- ✅ Service-to-service via REST
- ✅ No direct database access across services

### Service Discovery
- ✅ Eureka service registry
- ✅ Dynamic service URLs (lb://service-name)
- ✅ No hardcoded service addresses

### Circuit Breaker Pattern
- ✅ Resilience4j circuit breaker
- ✅ Fallback mechanisms
- ✅ Event tracking

### Timeouts & Resilience
- ✅ Connection timeout: 5 seconds
- ✅ Read timeout: 10 seconds
- ✅ Circuit breaker recovery: 15 seconds

### Monitoring
- ✅ Health endpoints (/actuator/health)
- ✅ Metrics endpoints (/actuator/metrics)
- ✅ Prometheus format support
- ✅ Logging with appropriate levels

### API Versioning
- Ready for v1, v2 URLs
- Example: `/v1/books`, `/v2/books`
- Currently using unversioned (ready for future)

### Documentation
- ✅ OpenAPI 3.0 specification
- ✅ Swagger UI interactive docs
- ✅ Auto-generated from code

---

## 🧪 Testing Workflows

### Smoke Test (5 minutes)
```
1. Check Eureka: GET {{eurekaServer}}/
2. Check Health: GET {{bookServiceDirect}}/actuator/health
3. Get Books: GET {{baseUrl}}/book-service/books
4. Get Subscriptions: GET {{baseUrl}}/subscription-service/subscriptions
5. Create Subscription: POST {{baseUrl}}/subscription-service/subscriptions
```

### Integration Test (15 minutes)
```
1. Create subscription via gateway
2. Check circuit breaker status
3. Get subscription by ID
4. Update book copies
5. Verify changes reflected
6. Check metrics
```

### Resilience Test (20 minutes)
```
1. Get subscriptions (baseline)
2. Stop Book Service
3. Try getting subscriptions (watch circuit open)
4. Wait 15 seconds
5. Restart Book Service
6. Verify recovery
7. Check circuit breaker events
```

### Load Test (30 minutes)
```
Using Postman Runner:
1. Run collection 100 times
2. Monitor response times
3. Check for errors
4. View metrics
5. Verify compression working
```

---

## 🔍 Common Issues & Troubleshooting

### Issue: Service not found (404)
**Cause**: Service not registered with Eureka  
**Solution**: 
- Check service is running
- Wait 30 seconds for registration
- Check Eureka dashboard at localhost:8761

### Issue: Connection timeout
**Cause**: Service not responding  
**Solution**: 
- Verify service is running
- Check port is correct
- Check firewall settings
- Increase timeout values

### Issue: Circuit breaker open
**Cause**: Too many failures in Book Service  
**Solution**: 
- Check Book Service health
- Wait 15 seconds for recovery
- Check circuit breaker events
- Restart service if needed

### Issue: Compression not working
**Cause**: Response too small  
**Solution**: 
- Compression only for ≥1KB responses
- GET /books with many records to see compression
- Check Content-Encoding header

---

## 📊 Performance Baseline

### Expected Response Times
- GET /books: 50-200ms
- POST /subscriptions: 100-300ms (with Book Service call)
- Circuit breaker fallback: <10ms

### Metrics to Monitor
- HTTP request count
- P95, P99 latency
- Error rate
- JVM memory usage
- Database connection pool usage

---

## 🚀 Next Steps

1. **Import collection** into Postman
2. **Configure environment** with your URLs
3. **Run smoke tests** to verify setup
4. **Explore error scenarios** to understand failures
5. **Test circuit breaker** behavior
6. **Monitor performance** with metrics endpoints
7. **Use Swagger UI** for endpoint documentation
8. **Set up Prometheus** for metrics collection

---

## 📞 Support

### Documentation
- README.md - Project overview
- UPGRADE_GUIDE.md - Migration details
- PROJECT_ANALYSIS_AND_VERIFICATION.md - Technical details

### API Documentation
- Swagger UI: http://localhost:8081/swagger-ui.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs

### Monitoring
- Eureka: http://localhost:8761
- Book Service Health: http://localhost:8081/actuator/health
- Metrics: http://localhost:8081/actuator/prometheus

---

**Last Updated**: January 17, 2026  
**Version**: 1.0  
**Status**: Production Ready ✅
