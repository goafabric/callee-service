package org.goafabric.calleeservice.aspect;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
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
public class CacheConfiguration extends CachingConfigurerSupport implements RuntimeHintsRegistrar {

    private Long cacheMaxSize = 1000l;

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

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        try { //caffeine hints
            hints.reflection().registerType(Class.forName("com.github.benmanes.caffeine.cache.SSMSA"), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(Class.forName("com.github.benmanes.caffeine.cache.PSAMS"), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        } catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }
}
