package org.goafabric.calleeservice.aspect;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
//@TestAnnotation
@CacheConfig(cacheNames = "test")
@RegisterReflectionForBinding({TestComponent.Foo.class, TestComponent.Call.class, TestComponent.Bar.class})
public class TestComponent {
    @Cacheable
    public Call callOnMe(String id) {
        System.err.println("cache missed, inside callOnMe");
        return new Call(id);
    }

    @Cacheable
    public Foo getFoo(String id) {
        return new Foo(id);
    }

    @Cacheable
    public Bar getBar(String id) {
        return new Bar(id);
    }


    record Call(String id) {}
    record Foo(String id) {}
    record Bar(String id) {}
}
