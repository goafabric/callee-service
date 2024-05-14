package org.goafabric.calleeservice.extensions;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.ObservationPredicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.ServerHttpObservationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Component
public class HttpInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Configuration
    static class Configurer implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new HttpInterceptor());
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TenantContext.setContext(request);
        configureLogsAndTracing(request);

        if (handler instanceof HandlerMethod handlerMethod) {
            log.info(" {} method called for user {} ", handlerMethod.getShortLogMessage(), TenantContext.getUserName());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.removeContext();
        MDC.remove("tenantId");
    }

    private static void configureLogsAndTracing(HttpServletRequest request) {
        MDC.put("tenantId", TenantContext.getTenantId());
        ServerHttpObservationFilter.findObservationContext(request).ifPresent(
                context -> context.addHighCardinalityKeyValue(KeyValue.of("tenant.id", TenantContext.getTenantId())));
    }

    @Bean @ConditionalOnMissingClass("org.springframework.security.oauth2.client.OAuth2AuthorizationContext")
    public SecurityFilterChain filterChain(HttpSecurity http, @Value("${security.authentication.enabled:true}") boolean isAuthenticationEnabled, HandlerMappingIntrospector introspector) throws Exception {
        return isAuthenticationEnabled
                ? http.authorizeHttpRequests(auth -> auth.requestMatchers(new MvcRequestMatcher(introspector, "/actuator/**")).permitAll().anyRequest().authenticated())
                .httpBasic(httpBasic -> {}).csrf(AbstractHttpConfigurer::disable).build()
                : http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).httpBasic(httpBasic -> {}).csrf(AbstractHttpConfigurer::disable).build();
    }

    @Bean @SuppressWarnings("java:S1874")
    PasswordEncoder passwordEncoder() { return NoOpPasswordEncoder.getInstance(); }

    @Bean
    ObservationPredicate disableHttpServerObservationsFromName() { return (name, context) -> !(name.startsWith("spring.security.") || (context instanceof ServerRequestObservationContext serverContext && (serverContext).getCarrier().getRequestURI().startsWith("/actuator"))); }

}