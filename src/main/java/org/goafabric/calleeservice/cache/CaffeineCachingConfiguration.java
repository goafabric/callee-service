/*
package org.goafabric.calleeservice.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
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
public class CaffeineCachingConfiguration implements CachingConfigurer {

    private Long cacheMaxSize = 1000l;

    private Long cacheExpiry = 10l;

    @Bean
    public CacheManager cacheManager() {
        final CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterAccess(cacheExpiry, TimeUnit.MINUTES));
        return cacheManager;
    }

    @Bean
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
            hints.reflection().registerType(TypeReference.of("com.github.benmanes.caffeine.cache.PSAMS"),
                    builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));
            hints.reflection().registerType(TypeReference.of("com.github.benmanes.caffeine.cache.SSMS"),
                    builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));
            hints.reflection().registerType(TypeReference.of("com.github.benmanes.caffeine.cache.SSMSA"),
                    builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));
            hints.reflection().registerType(TypeReference.of("com.github.benmanes.caffeine.cache.SSMS"),
                    builder -> builder.withField("FACTORY"));
            hints.reflection().registerType(TypeReference.of("com.github.benmanes.caffeine.cache.SSMSA"),
                    builder -> builder.withField("FACTORY"));
        }
    }
}
*/
