package org.goafabric.calleeservice.cache;


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
import org.springframework.context.annotation.ImportRuntimeHints;

import java.util.concurrent.TimeUnit;

//implementation("com.github.ben-manes.caffeine:caffeine"); implementation("org.springframework.boot:spring-boot-starter-cache");

@Configuration
@EnableCaching
@ImportRuntimeHints(CaffeineCachingConfiguration.CacheRuntimeHints.class)
public class CaffeineCachingConfiguration extends CachingConfigurerSupport {

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
            var tenantId = "0"; //HttpInterceptor.getTenantID()
            var organizationId = "1"; //HttpInterceptor.getOrganizationId()
            return new SimpleKey(tenantId, organizationId, method.getName(), params);
        };
    }

    static class CacheRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            try { //caffeine hints
                hints.reflection().registerType(Class.forName("com.github.benmanes.caffeine.cache.SSMSA"), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
                hints.reflection().registerType(Class.forName("com.github.benmanes.caffeine.cache.PSAMS"), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}



