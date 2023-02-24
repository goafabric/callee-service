package org.goafabric.calleeservice.aspect;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {

    //@Value("${cache.maxsize}")
    private Long cacheMaxSize = 1000l;

    //@Value("${cache.expiry}")
    private Long cacheExpiry = 10l;

    @Bean
    @Override
    public CacheManager cacheManager() {
        final CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterAccess(cacheExpiry, TimeUnit.MINUTES));
        return cacheManager;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            final String tenantId = "0"; //HttpInterceptor.getTenantID()
            return new SimpleKey(tenantId, method.getName(), params);
        };
    }
}
