#Working
Core
- Web, ExceptionHandler
- Lombok, Mapstruct

- Health, Prometheus
- Tracing
- OpenAPI

- Security

- DurationLogger Aspect

# Oracle GraalVM LOCAL Native Compile Times per Service Type

- Service with JPA + Monitoring + Openapi: 2:45m
- Service withOUT JPA + Monitoring + Openapi: 1:30m
- Service naked Web Only: 1:10m
- Service Console Only: 43sec
- 
- Service with JPA inside Liberica GraalVm : 4m+           

# Github Paketo Liberica Build Times
- Service with JPA: 16m
- Service without JPA: 10m
                              
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
