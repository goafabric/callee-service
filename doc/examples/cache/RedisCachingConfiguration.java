package org.goafabric.calleeservice.cache;


import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

//implementation("org.springframework.boot:spring-boot-starter-data-redis"); implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml"); implementation("org.springframework.boot:spring-boot-starter-cache");
//docker run --name redis --rm -p6379:6379 -p16379:16379 redis:7.2.1

@Configuration
@EnableCaching
//@RegisterReflectionForBinding({TestComponent.Foo.class, TestComponent.Call.class, TestComponent.Bar.class}) //classes that are cached need to be registered for Reflection
public class RedisCachingConfiguration implements CachingConfigurer {

    private Long cacheMaxSize = 1000l;

    private Long cacheExpiry = 10l;

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(cacheExpiry))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            var tenantId = "0"; //HttpInterceptor.getTenantID()
            var organizationId = "1"; //HttpInterceptor.getOrganizationId()
            return new SimpleKey(tenantId, organizationId, method.getName(), params);
        };
    }
}


