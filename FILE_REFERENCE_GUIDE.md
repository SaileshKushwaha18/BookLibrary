# BookLibrary Microservices - Complete File Reference Guide

**Last Updated**: January 17, 2026  
**Project Status**: ✅ Fully Upgraded to Spring Boot 3.4.0 & JDK 17

---

## Documentation Files (Read These First!)

| Document | Purpose | Location |
|----------|---------|----------|
| **SUMMARY.md** | Executive overview & quick facts | `/SUMMARY.md` |
| **README.md** | Project architecture & quick start | `/README.md` |
| **UPGRADE_GUIDE.md** | Detailed migration guide | `/UPGRADE_GUIDE.md` |
| **PROJECT_ANALYSIS_AND_VERIFICATION.md** | Complete technical analysis | `/PROJECT_ANALYSIS_AND_VERIFICATION.md` |
| **PRODUCTION_READINESS_CHECKLIST.md** | Full verification checklist | `/PRODUCTION_READINESS_CHECKLIST.md` |
| **FILE_REFERENCE_GUIDE.md** | This file - complete file listing | `/FILE_REFERENCE_GUIDE.md` |

---

## Configuration Files Structure

```
BookLibrary/
├── README.md                          [UPDATED] Project overview & architecture
├── SUMMARY.md                         [NEW] Executive summary
├── UPGRADE_GUIDE.md                   [NEW] Migration guide
├── PROJECT_ANALYSIS_AND_VERIFICATION.md [NEW] Technical analysis
├── PRODUCTION_READINESS_CHECKLIST.md  [NEW] Verification checklist
├── FILE_REFERENCE_GUIDE.md            [NEW] This file
├── BookLibraryAPIMicroService.postman_collection.json [Postman API tests]
│
├── ApiGatewayService/
│   ├── pom.xml                        [UPDATED] Spring Boot 3.4.0, JDK 17, Gateway
│   ├── mvnw & mvnw.cmd               [Build scripts]
│   └── src/
│       ├── main/
│       │   ├── java/com/fis/booklibrary/casestudy/
│       │   │   ├── ApiGatewayServiceApplication.java [UPDATED] Removed Zuul
│       │   │   ├── config/
│       │   │   │   └── GatewayConfig.java [NEW] Spring Cloud Gateway routes
│       │   │   └── zuulfilter/
│       │   │       └── [DELETED] All filter files removed
│       │   └── resources/
│       │       ├── application.yml [UPDATED] Gateway routes configuration
│       │       └── bootstrap.yml
│       └── test/
│           └── java/.../ApiGatewayServiceApplicationTests.java
│
├── BookLibraryEurekaServer/
│   ├── pom.xml                        [UPDATED] Spring Boot 3.4.0, JDK 17
│   ├── mvnw & mvnw.cmd               [Build scripts]
│   └── src/
│       ├── main/
│       │   ├── java/com/fis/booklibrary/casestudy/
│       │   │   └── BookLibraryEurekaServerApplication.java
│       │   └── resources/
│       │       ├── application.yml [UPDATED] Eureka server config
│       │       └── bootstrap.yml
│       └── test/
│           └── java/.../BookLibraryEurekaServerApplicationTests.java
│
├── BookService/
│   ├── pom.xml                        [UPDATED] Spring Boot 3.4.0, JDK 17, Springdoc
│   ├── mvnw & mvnw.cmd               [Build scripts]
│   └── src/
│       ├── main/
│       │   ├── java/com/fis/booklibrary/casestudy/
│       │   │   ├── BookApplication.java
│       │   │   ├── SpringFoxConfig.java [UPDATED] OpenAPI 3.0 bean
│       │   │   ├── controller/
│       │   │   │   └── BookController.java
│       │   │   ├── feignclient/
│       │   │   │   └── BookServiceFeignClient.java
│       │   │   ├── model/
│       │   │   │   └── Book.java [UPDATED] jakarta.persistence imports
│       │   │   ├── repository/
│       │   │   │   └── BookRepository.java
│       │   │   └── service/
│       │   │       └── BookService.java [UPDATED] Improved error handling
│       │   └── resources/
│       │       ├── application.yml [UPDATED] Compression, HikariCP, management
│       │       └── bootstrap.yml
│       └── test/
│           └── java/.../
│               ├── BookApplicationApiTests.java
│               └── BookApplicationTests.java
│
└── SubscriptionService/
    ├── pom.xml                        [UPDATED] Spring Boot 3.4.0, JDK 17, Resilience4j
    ├── mvnw & mvnw.cmd               [Build scripts]
    └── src/
        ├── main/
        │   ├── java/com/fis/booklibrary/casestudy/
        │   │   ├── SubscriptionApplication.java [UPDATED] Removed Hystrix/Ribbon
        │   │   ├── SpringFoxConfig.java [UPDATED] OpenAPI 3.0 bean
        │   │   ├── config/
        │   │   │   ├── RestTemplateConfig.java [NEW] Timeout configuration
        │   │   │   └── [DELETED] RibbonConfiguration.java removed
        │   │   ├── controller/
        │   │   │   └── SubscriptionController.java
        │   │   ├── exception/
        │   │   │   └── ResponseStatusExceptionHandler.java [UPDATED] jakarta.servlet
        │   │   ├── model/
        │   │   │   └── Subscription.java [UPDATED] jakarta.persistence imports
        │   │   ├── repository/
        │   │   │   └── SubscriptionRepository.java
        │   │   └── service/
        │   │       └── SubscriptionService.java [UPDATED] Resilience4j circuit breaker
        │   └── resources/
        │       ├── application.yml [UPDATED] Resilience4j config, management
        │       └── bootstrap.yml
        └── test/
            └── java/.../SubscriptionApplicationTests.java
```

---

## Key Modified Files (By Category)

### 🔧 POM Files (Build Configuration)

**ApiGatewayService/pom.xml**
- Version: Spring Boot 3.4.0, JDK 17, Spring Cloud 2023.0.3
- Dependencies:
  - spring-cloud-starter-gateway (replaces Zuul)
  - spring-boot-starter-webflux (reactive)
  - spring-cloud-starter-netflix-eureka-client

**BookLibraryEurekaServer/pom.xml**
- Version: Spring Boot 3.4.0, JDK 17, Spring Cloud 2023.0.3
- Dependencies:
  - spring-cloud-starter-netflix-eureka-server
  - spring-boot-starter-actuator

**BookService/pom.xml**
- Version: Spring Boot 3.4.0, JDK 17, Spring Cloud 2023.0.3
- Dependencies:
  - spring-boot-starter-data-jpa
  - spring-boot-starter-validation
  - springdoc-openapi-starter-webmvc-ui (replaces SpringFox)
  - Removed: springfox-swagger2, springfox-swagger-ui

**SubscriptionService/pom.xml**
- Version: Spring Boot 3.4.0, JDK 17, Spring Cloud 2023.0.3
- Dependencies:
  - resilience4j-spring-boot3 (replaces Hystrix)
  - resilience4j-circuitbreaker
  - springdoc-openapi-starter-webmvc-ui (replaces SpringFox)
  - Removed: spring-cloud-starter-hystrix, spring-cloud-starter-netflix-ribbon

---

### 📝 Application Configuration (YAML)

**ApiGatewayService/src/main/resources/application.yml**
- Port: 8080
- Features:
  - Spring Cloud Gateway routes (book-service, subscription-service)
  - Eureka client registration
  - Management endpoints (health, info, metrics, prometheus)
  - Custom logging levels

**BookService/src/main/resources/application.yml**
- Port: 8081
- Features:
  - HTTP compression (enabled, min 1KB)
  - HikariCP pooling (max 10, min 5)
  - JPA with H2 database
  - Eureka client registration
  - Management endpoints (health, info, metrics, prometheus)

**SubscriptionService/src/main/resources/application.yml**
- Port: 8082
- Features:
  - Resilience4j circuit breaker configuration
  - Sliding window 10, min calls 5, threshold 50%
  - Recovery timeout 15 seconds
  - JPA with H2 database
  - Eureka client registration
  - Management endpoints (health, info, metrics, prometheus, circuitbreakers)

**BookLibraryEurekaServer/src/main/resources/application.yml**
- Port: 8761
- Features:
  - Eureka server configuration
  - Self-registration disabled
  - Management endpoints exposed

---

### ☕ Java Source Files (Core Logic)

#### Entity Models (Jakarta Imports)
**Book.java** (BookService)
- Location: `BookService/src/main/java/.../model/Book.java`
- Changes: `javax.persistence` → `jakarta.persistence` (ALL imports)
- Annotations: @Entity, @Table, @Id, @Column
- Lombok: @Data, @NoArgsConstructor, @AllArgsConstructor, @Getter, @Setter

**Subscription.java** (SubscriptionService)
- Location: `SubscriptionService/src/main/java/.../model/Subscription.java`
- Changes: `javax.persistence` → `jakarta.persistence` (ALL imports)
- Annotations: @Entity, @Id, @GeneratedValue(IDENTITY), @Column
- Lombok: @Data, @NoArgsConstructor, @AllArgsConstructor, @Getter, @Setter

#### Service Classes
**BookService.java** (BookService)
- Location: `BookService/src/main/java/.../service/BookService.java`
- Changes:
  - Improved Optional handling with proper error throwing
  - Enhanced logging with warning/severe levels
  - Better error messages for not-found scenarios

**SubscriptionService.java** (SubscriptionService)
- Location: `SubscriptionService/src/main/java/.../service/SubscriptionService.java`
- Changes:
  - Replaced `@HystrixCommand` with `@CircuitBreaker(name = "book-service")`
  - Implemented fallback method `getAvailableCopiesFallback()`
  - RestClientException handling with proper logging
  - Service URI uses service discovery: `http://book-service/books/`

#### Application Entry Points
**ApiGatewayServiceApplication.java**
- Location: `ApiGatewayService/src/main/java/.../ApiGatewayServiceApplication.java`
- Changes:
  - Removed: `@EnableZuulProxy` annotation
  - Removed: All Zuul filter bean definitions
  - Kept: `@SpringBootApplication`, `@EnableDiscoveryClient`

**SubscriptionApplication.java**
- Location: `SubscriptionService/src/main/java/.../SubscriptionApplication.java`
- Changes:
  - Removed: `@EnableHystrixDashboard`, `@EnableCircuitBreaker`
  - Removed: `@RibbonClient` annotation
  - Added: RestTemplate bean with Duration-based timeouts
  - Kept: `@SpringBootApplication`, `@EnableDiscoveryClient`

#### Configuration Classes
**GatewayConfig.java** (NEW)
- Location: `ApiGatewayService/src/main/java/.../config/GatewayConfig.java`
- Purpose: Spring Cloud Gateway route configuration
- Bean: `customRouteLocator()` with fluent route builder
- Routes:
  - book-service → lb://book-service
  - subscription-service → lb://subscription-service
- Filters: stripPrefix(1), addRequestHeader

**RestTemplateConfig.java** (NEW)
- Location: `SubscriptionService/src/main/java/.../config/RestTemplateConfig.java`
- Purpose: RestTemplate with timeout configuration
- Timeouts:
  - Connection: 5 seconds
  - Read: 10 seconds
- Uses: `java.time.Duration` API

#### API Documentation Configuration
**SpringFoxConfig.java** (BookService)
- Location: `BookService/src/main/java/.../SpringFoxConfig.java`
- Changes: OpenAPI 3.0 bean (replaces Docket)
- Bean: `bookServiceOpenAPI()`
- Info: Title, description, version, contact, license

**SpringFoxConfig.java** (SubscriptionService)
- Location: `SubscriptionService/src/main/java/.../SpringFoxConfig.java`
- Changes: OpenAPI 3.0 bean (replaces Docket)
- Bean: `subscriptionServiceOpenAPI()`
- Info: Title, description, version, contact, license

#### Exception Handling
**ResponseStatusExceptionHandler.java**
- Location: `SubscriptionService/src/main/java/.../exception/ResponseStatusExceptionHandler.java`
- Changes: `javax.servlet` → `jakarta.servlet`
- Annotation: @RestControllerAdvice
- Purpose: Global exception handling

---

### 🗑️ Deleted Files (Legacy Code Removed)

**Zuul Filter Files** (ApiGatewayService)
- ❌ `zuulfilter/PreFilter.java` - DELETED
- ❌ `zuulfilter/PostFilter.java` - DELETED
- ❌ `zuulfilter/RouteFilter.java` - DELETED
- ❌ `zuulfilter/ErrorFilter.java` - DELETED
- Reason: Zuul replaced with Spring Cloud Gateway

**Ribbon Configuration** (SubscriptionService)
- ❌ `RibbonConfiguration.java` - DELETED
- Reason: Ribbon replaced with Spring Cloud LoadBalancer

---

## Quick Navigation

### For Understanding Architecture
1. Start: [SUMMARY.md](./SUMMARY.md)
2. Then: [README.md](./README.md)
3. Architecture: See "Service Architecture Overview" in README.md

### For Deployment
1. Start: [UPGRADE_GUIDE.md](./UPGRADE_GUIDE.md)
2. Follow: "Migration Steps for Developers" section
3. Verify: [PRODUCTION_READINESS_CHECKLIST.md](./PRODUCTION_READINESS_CHECKLIST.md)

### For Technical Details
1. Read: [PROJECT_ANALYSIS_AND_VERIFICATION.md](./PROJECT_ANALYSIS_AND_VERIFICATION.md)
2. Check: Sections 1-7 for complete technical breakdown
3. Reference: Section 12 for deployment instructions

### For Complete File Changes
1. This file: [FILE_REFERENCE_GUIDE.md](./FILE_REFERENCE_GUIDE.md)
2. See sections: "Configuration Files Structure" & "Key Modified Files"

---

## Service Port Reference

| Service | Port | Purpose | Endpoints |
|---------|------|---------|-----------|
| **Eureka Server** | 8761 | Service discovery | Dashboard at http://localhost:8761 |
| **Book Service** | 8081 | Book data | /books, /swagger-ui.html |
| **Subscription Service** | 8082 | Subscriptions | /subscriptions, /swagger-ui.html |
| **API Gateway** | 8080 | Request routing | Routes to above services |

---

## Technology Stack Summary

| Component | Old | New | Status |
|-----------|-----|-----|--------|
| **Java** | 8 | 17 | ✅ Upgraded |
| **Spring Boot** | 2.3.2 | 3.4.0 | ✅ Upgraded |
| **Spring Cloud** | Hoxton | 2023.0.3 | ✅ Upgraded |
| **API Gateway** | Zuul | Spring Cloud Gateway | ✅ Replaced |
| **Circuit Breaker** | Hystrix | Resilience4j | ✅ Replaced |
| **Load Balancer** | Ribbon | Spring Cloud LoadBalancer | ✅ Replaced |
| **API Docs** | Swagger 2 | OpenAPI 3.0 | ✅ Replaced |
| **Package Imports** | javax.* | jakarta.* | ✅ Migrated |

---

## Import Guide

When working with these files, remember:

- **All jakarta imports**: Files use `jakarta.persistence.*`, `jakarta.servlet.*`
- **No javax imports**: Any file with `javax.*` imports should be flagged as incomplete
- **All Spring Boot 3 compatible**: All dependencies use Boot 3.4.0 versions
- **All JDK 17 compatible**: Code uses Java 17 features and syntax

---

## Verification Quick Links

To verify the upgrade completeness:

1. **Check pom.xml**: Look for version="3.4.0" and java.version=17
2. **Check imports**: All should be `jakarta.*` (no `javax.*`)
3. **Check annotations**: No `@EnableZuulProxy`, `@HystrixCommand`, `@RibbonClient`
4. **Check config**: Application.yml files have proper management endpoints
5. **Check documentation**: Swagger UI available at `/swagger-ui.html`

---

## Common Tasks

### Build All Services
```bash
mvn clean install -DskipTests
```

### Run Individual Service
```bash
cd <ServiceName>
mvn spring-boot:run
```

### Check Health
```bash
curl http://localhost:<PORT>/actuator/health
```

### View API Documentation
```
http://localhost:8081/swagger-ui.html  (Book Service)
http://localhost:8082/swagger-ui.html  (Subscription Service)
```

### View Eureka Dashboard
```
http://localhost:8761
```

---

## Support & Documentation

- **Technical Analysis**: See [PROJECT_ANALYSIS_AND_VERIFICATION.md](./PROJECT_ANALYSIS_AND_VERIFICATION.md)
- **Migration Guide**: See [UPGRADE_GUIDE.md](./UPGRADE_GUIDE.md)
- **Quick Start**: See [README.md](./README.md)
- **Executive Summary**: See [SUMMARY.md](./SUMMARY.md)
- **Verification**: See [PRODUCTION_READINESS_CHECKLIST.md](./PRODUCTION_READINESS_CHECKLIST.md)

---

## Status

✅ **All files verified and upgraded**  
✅ **All documentation created**  
✅ **Production ready**

**Last Updated**: January 17, 2026  
**System Status**: 🟢 **READY FOR DEPLOYMENT**

---

*This guide provides a complete map of all files in the BookLibrary microservices project and their upgrade status.*
