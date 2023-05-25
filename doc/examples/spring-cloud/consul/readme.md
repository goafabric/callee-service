# dependency
implementation("org.springframework.cloud:spring-cloud-starter-consul-all:4.0.2")

# docker
docker run --rm -p 8500:8500 -p 8600:8600/udp --name=consul consul:1.15.2 agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0

# application.yaml
spring.cloud.consul.config.enabled: "false"

# links
https://www.baeldung.com/spring-cloud-consul
https://medium.com/javarevisited/hands-on-consul-with-spring-boot-1ebf2918165c
