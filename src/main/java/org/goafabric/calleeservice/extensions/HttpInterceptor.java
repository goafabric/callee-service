package org.goafabric.calleeservice.extensions;

import io.micrometer.common.KeyValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.ServerHttpObservationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

}