# dependency
implementation("org.springframework.cloud:spring-cloud-starter-consul-all:4.0.2")
implementation("org.springframework.boot:spring-boot-starter-webflux")
                                                        
# props
spring.cloud.consul.config.enabled: false
spring.cloud.consul.enabled: true
spring.cloud.bus.enabled: ${spring.cloud.consul.enabled}