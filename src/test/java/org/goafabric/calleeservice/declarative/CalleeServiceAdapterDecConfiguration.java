package org.goafabric.calleeservice.declarative;


import org.goafabric.calleeservice.crossfunctional.HttpInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Base64;

@Configuration
public class CalleeServiceAdapterDecConfiguration {
    
    @Bean
    CalleeServiceAdapterDec calleeServiceAdapterDec(
            @Value("${adapter.calleeservice.url}") String url,
            @Value("${adapter.calleeservice.user}") String user,
            @Value("${adapter.calleeservice.password}") String password,
            @Value("${adapter.timeout}") Integer timeout) throws Exception {

        final HttpServiceProxyFactory proxyFactory = WebClientAdapter.createHttpServiceProxyFactory(
            WebClient.builder()
                    .baseUrl(url)
                    .defaultHeaders(header -> header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .defaultHeaders(header -> header.setBasicAuth(new String(Base64.getDecoder().decode(user)), new String(Base64.getDecoder().decode(password))))
                    .defaultHeaders(header -> header.set("X-TenantId", HttpInterceptor.getTenantId()))
                    .defaultHeaders(header -> header.set("X-Auth-Request-Preferred-Username", HttpInterceptor.getUserName()))
                    .build()
        );

        //proxyFactory.setBlockTimeout(Duration.ofSeconds(timeout));
        proxyFactory.afterPropertiesSet();

        return proxyFactory
                .createClient(CalleeServiceAdapterDec.class);
    }

}
