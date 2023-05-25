# dependency
implementation("org.springframework.cloud:spring-cloud-starter-consul-all:4.0.2")
implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.2")

# application.yaml
spring.cloud.consul.config.enabled: "false"
#spring.cloud.openfeign.client.config.callee-service.url: "http://localhost:50900"