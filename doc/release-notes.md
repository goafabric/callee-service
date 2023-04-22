# 3.1.0
- upgrade to Spring Boot 3.1.0
- maven build (re) moved to doc/maven in favour of gradle
- kicked lombok in favour of java records
- converted gradle build file to kotlin dsl

- upgrade to springdoc 2.1.0
- upgrade to mapstruct 1.5.4

# 3.0.3
- upgrade to Spring Boot 3.0.3
- spring cloud kicked out

# 3.0.2
- upgrade to Spring Boot 3.0.2

# 3.0.0
- upgrade to Spring Boot 3.0.0

# 2.0.1
- upgrade to Spring Boot 2.7.5
- upgrade to Spring Cloud 2021.0.4
- upgrade to openapi 1.6.12

- update jib plugin to version 3.3.0
- upgrade to ibm semeru baseimage 17.0.4

# 2.0.0
- Parity with Spring Boot Version

- owasp added to parent pom
- upgrade to Spring Boot 2.7.3
- upgrade to Spring Native 0.12.1

- Gradle build added

# 1.2.3
- upgrade to Spring Boot 2.7.1   
- upgrade to Spring Native 0.12.0
- upgrade to Spring Cloud 2021.0.3
- upgrade to JDK 17.0.3

# 1.2.2
- upgrade to Spring Boot 2.6.6
- upgrade to Spring Native 0.11.4
- upgrade to Lombok 1.18.22
- HttpInterceptor for Username and TenantId

# 1.2.1
- upgrade to Spring Boot 2.6.3
- upgrade to Spring Native 0.11.2
- Update to OpenAPI 1.6.4 + JIB 3.2.0

- Java 17 IBM Semeru Runtime for JVM Images
- Java 17 Compiler Level and Native Image
- Multi Arch JVM Images

# 1.2.0
- plugin management
- upgrade to Spring Native 0.11
- upgrade to Spring Boot 2.6.1
- upgrade to Spring Cloud 2021.0.0
- build updated to jdk 17

- OpenJ9HeapDump Endpoint removed, as it is now supported by Spring Boot 2.6.0                               

- Spring REST Variables now explicitly defined (e.g. @PathVariable("name")), as Name Inference seems to be removed from native 0.11
- Changes to SecurityConfiguration, as @ConditionalProperty seems to be resolved at build time now for native
- DurationLog Aspect added    

# 1.1.0
- Upgrade to Spring Boot 2.5.3 / Spring Native 0.10.3
- Swagger added
- spring sleuth for jaeger added

# 1.0.7
- upgrade to Spring Boot 2.52 and Spring Native 0.10.1
- added CalleService.sayMyName instead of isAlive

# 1.0.6
- OpenJ9 Heapdump added
- Upgrade to Spring Boot 2.5.1 and Spring Native 0.10.0

# 1.0.5
- sync with quarkus variant

# 1.0.4
- Typo in Callee fixed

# 1.0.3
- Upgrade to Spring Boot 2.5.0
- Upgrade to Java Language Level 11 
- Docker Build Integrity Check added

# 1.0.2
- Prometheus Metrics readded 
- Jaeger and Zipkin Tryouts (deactivated)

# 1.0.1
- whole docker container handling reworked
- isAlive latency added
- graalvm spring-native image build in GITHUB added 

# 1.0.0
- initial version
