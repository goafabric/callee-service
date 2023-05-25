package org.goafabric.calleeservice.client;


import org.goafabric.calleeservice.crossfunctional.HttpInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
//@RegisterReflectionForBinding(Callee.class)
public class CalleeServiceAdapterDecConfiguration {

    @Bean
    @Lazy
    public CalleeServiceAdapterDec calleeServiceAdapterDec(
            @Value("${spring.cloud.consul.enabled}") Boolean enabled,
            DiscoveryClient discoveryClient) throws Exception {

        final String discoveryUrl = enabled
                ? discoveryClient.getInstances("callee-service").get(0).getUri().toString()
                : "http://localhost:50900";

        final WebClient client = WebClient.builder()
                .baseUrl(discoveryUrl)
                .defaultHeaders(header -> header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .defaultHeaders(header -> header.setBasicAuth("admin", "admin"))
                .defaultHeaders(header -> header.set("X-TenantId", HttpInterceptor.getTenantId()))
                .defaultHeaders(header -> header.set("X-Auth-Request-Preferred-Username", HttpInterceptor.getUserName()))
                .build();

        final HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
        return proxyFactory.createClient(CalleeServiceAdapterDec.class);
    }
}


