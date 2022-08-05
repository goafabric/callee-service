package org.goafabric.calleeservice.adapter;

import org.goafabric.calleeservice.service.Callee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CalleeServiceAdapter {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${adapter.calleeservice.url}")
    private String url;

    public Callee sayMyName(String name) {
        final Callee callee = restTemplate.getForObject(url + "/callees/sayMyName?name={name}", Callee.class, name);
        return callee;
    }
}
