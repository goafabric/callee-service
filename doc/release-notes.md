#1.0.2
- Prometheus Metrics readded 

#1.0.1
- whole docker container handling reworked
- isAlive latency added
- graalvm spring-native image build in GITHUB added 
    - working: Tomcat, Actuator, Security, ExceptionHandler, Lombok, Prometheus
    - not working: Annotations, Aspects, Cache, Swagger / Flyway / Resilience4j, Tracing
    - not tested: Validation, Jasypt

#1.0.0
- initial version