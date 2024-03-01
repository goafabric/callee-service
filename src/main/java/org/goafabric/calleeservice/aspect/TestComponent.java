package org.goafabric.calleeservice.aspect;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@TestAnnotation
@CacheConfig(cacheNames = "test")
public class TestComponent {
    @Cacheable
    public Call callOnMe(String id) {
        System.err.println("inside callOnMe");
        return new Call("0");
    }

    @Cacheable
    public Foo getFoo(String id) {
        return new Foo("1");
    }

    @Cacheable
    public Bar getBar(String id) {
        return new Bar("2");
    }


    private record Call(String id) {}
    private record Foo(String id) {}
    private record Bar(String id) {}
}
