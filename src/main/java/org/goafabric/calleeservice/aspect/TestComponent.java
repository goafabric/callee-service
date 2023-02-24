package org.goafabric.calleeservice.aspect;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@TestAnnotation
@CacheConfig(cacheNames = "test")
public class TestComponent {
    @Cacheable
    public void callOnMe(String id) {
        System.err.println("inside method");
    }
}
