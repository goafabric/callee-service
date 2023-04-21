package org.goafabric.calleeservice;

import org.goafabric.calleeservice.controller.Callee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

@Component
public class ConsulClient implements ApplicationListener<ApplicationReadyEvent> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("consul : " + getServiceUrl());
        log.info(sayMyName("hello from consul").message());
    }

    public String getServiceUrl() {
        return discoveryClient.getInstances("callee-service").get(0).getUri().toString();
    }


    public Callee sayMyName(String name) {
        return restTemplate.getForObject(getServiceUrl() + "/callees/sayMyName?name={name}", Callee.class, name);
    }

    @Configuration
    static class RestConfiguration {
        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder,
                                         @Value("${spring.security.user.name}") String user,
                                         @Value("${spring.security.user.password}") String password,
                                         @Value("${adapter.timeout:1000}") Integer timeout) {
            RestTemplate restTemplate = builder
                    .setConnectTimeout(Duration.ofMillis(timeout))
                    .setReadTimeout(Duration.ofMillis(timeout))
                    .build();

            restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                request.getHeaders().setBasicAuth(user, password);
                return execution.execute(request, body);
            });
            return restTemplate;
        }
    }

}
