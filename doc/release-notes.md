#1.0.3
- Upgrade to Spring Boot 2.5.0
- Upgrade to Java Language Level 11 
- Docker Build Integrity Check added

#1.0.2
- Prometheus Metrics readded 
- Jaeger and Zipkin Tryouts (deactivated)

#1.0.1
- whole docker container handling reworked
- isAlive latency added
- graalvm spring-native image build in GITHUB added 
    - working: Tomcat, Actuator, Security, ExceptionHandler, Lombok, Prometheus, Bean Validation
    - not working: Annotations (e.g. @Transactional), Aspects, Cache, Swagger / Flyway / Resilience4j, Tracing

#1.0.0
- initial version
