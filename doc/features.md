# Working
## Core
- Web, ExceptionHandler
- Lombok, Mapstruct

- Health, Prometheus
- Tracing
- OpenAPI

- Security
- DurationLogger Aspect
- Bean Validation

## Adapter
- REST Call
- CircuitBreaker

## Persistence
- JPA
- Auditing, Multi Tenancy
 
# Local Graalvm build Times 
- Service without JPA: 1m 40s 
- Service with JPA: 3m 20s (2m on Pro)

# Github Paketo Liberica Build Times
- Service without JPA: 10m
- Service with JPA: 16m
                              
# Memory
- Eclipse Temurin 17 Tomcat Application : 270MB 
- Eclipse Temurin 17 JPA Application    : 450MB

- IBM Semuru 17 Tomcat Application      : 140MB
- IBM Semuru 17 JPA Application         : 250MB
- => ~ 45% less than Temurin

- Spring Native Tomcat Application      : 35MB
- Spring Native JPA Application         : 55MB
- => ~ 75% less than Semeru

- Quarkus Native Tomcat Application     : 8MB
- Quarkus Native JPA Application        : 15MB
- => ~ 75% less than Spring Native
        
# CPU Usage Simple REST App (JVM / Spring Native / GO)
- 10 req/s :  8%  / 8%  / 0,4%
- 100 req/s : 30% / 30% / 4%
- 500 req/s : 40% / 40% / 10%

# CPU Usage JPA App (JVM / Spring Native / Quarkus)
- 10 req/s  : 25% / 25% / 15%
- 100 req/s : 40% / 120% / 40%
- 500 req/s : 160% / cap at 100 req/s / cap at 250 req/s