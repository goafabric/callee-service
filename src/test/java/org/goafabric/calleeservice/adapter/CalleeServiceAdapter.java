package org.goafabric.calleeservice.adapter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.goafabric.calleeservice.controller.Callee;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//@Slf4j
@Component
@RegisterReflectionForBinding(Callee.class)
@CircuitBreaker(name = "calleeservice")
public class CalleeServiceAdapter {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${adapter.calleeservice.url}")
    private String url;

    public Callee sayMyName(String name) {
        final Callee callee = restTemplate.getForObject(url + "/callees/sayMyName?name={name}", Callee.class, name);
        return callee;
    }

    public Callee sayMyOtherName(String name) {
        final Callee callee = restTemplate.getForObject(url + "/callees/sayMyOtherName/{name}", Callee.class, name);
        return callee;
    }
}
