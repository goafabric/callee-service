# dependency
implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.1")
implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.1")

# docker
docker run --pull always --name eureka-server --rm -p8761:8761 goafabric/eureka-server:1.0.0-SNAPSHOT

# spring native support
spring.cloud.refresh.enabled: "false"
                                
# links
https://medium.com/javarevisited/hands-on-consul-with-spring-boot-1ebf2918165c