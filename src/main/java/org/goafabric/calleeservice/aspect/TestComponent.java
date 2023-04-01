package org.goafabric.calleeservice.aspect;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@TestAnnotation
@CacheConfig(cacheNames = "test")
public class TestComponent {
    @Cacheable
    public void callOnMe() {
        System.err.println("inside callOnMe");
    }

    @Cacheable
    public Foo getFoo(String id) {
        return new Foo();
    }

    @Cacheable
    public Bar getBar(String id) {
        return new Bar();
    }


    private static class Foo {}
    private static class Bar {}
}
