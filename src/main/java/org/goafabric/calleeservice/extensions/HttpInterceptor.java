package org.goafabric.calleeservice.extensions;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.ObservationPredicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.ServerHttpObservationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Component
public class HttpInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Configuration
    static class Configurer implements WebMvcConfigurer {
        private @Value("${cors.enabled:false}") boolean corsEnabled;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new HttpInterceptor());
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            if (!corsEnabled) { registry.addMapping("/**").allowedOrigins("*").allowedMethods("*"); }
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
    SecurityFilterChain filterChain(HttpSecurity http, @Value("${security.authentication.enabled:false}") boolean isAuthenticationEnabled, HandlerMappingIntrospector introspector) throws Exception {
        return isAuthenticationEnabled
                ? http.authorizeHttpRequests(auth -> auth.requestMatchers(new MvcRequestMatcher(introspector, "/actuator/**")).permitAll().anyRequest().authenticated())
                .httpBasic(httpBasic -> {}).csrf(AbstractHttpConfigurer::disable).build()
                : http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).httpBasic(httpBasic -> {}).csrf(AbstractHttpConfigurer::disable).build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override public String encode(CharSequence rawPassword) { return rawPassword.toString(); }
            @Override public boolean matches(CharSequence rawPassword, String encodedPassword) { return rawPassword.toString().equals(encodedPassword);}
        };
    }

    @Bean
    ObservationPredicate disableHttpServerObservationsFromName() { return (name, context) -> !(name.startsWith("spring.security.") || (context instanceof ServerRequestObservationContext serverContext && (serverContext).getCarrier().getRequestURI().startsWith("/actuator"))); }

    @Value("${multi-tenancy.schema-prefix}") private String schemaPrefix;
    @RegisterReflectionForBinding(HttpInterceptor.class)
    public String getPrefix() { return schemaPrefix + TenantContext.getTenantId() + "_"; }
}