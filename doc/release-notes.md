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
