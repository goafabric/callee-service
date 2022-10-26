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

- Service with JPA + Monitoring + Openapi 2:45m
- Service withOUT JPA + Monitoring + Openapi 1:30m
- Service naked Web Only 1:10m
- Service Console Only 43sec
- 
- Service with JPA inside Liberica GraalVm : 4m+                                
