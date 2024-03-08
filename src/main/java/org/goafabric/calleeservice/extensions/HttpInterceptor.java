package org.goafabric.calleeservice.extensions;

import io.micrometer.common.KeyValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.ServerHttpObservationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;


public class HttpInterceptor implements HandlerInterceptor {
    record TenantContext(String tenantId, String organizationId, String userName) {
        public Map<String, String> toMap() {
            return Map.of("X-TenantId", tenantId, "X-OrganizationId", organizationId, "X-UserName", userName);
        }
        //public String tenantId() { return tenantId != null ? tenantId : "0"; }
        //public String organizationId() { return organizationId != null ? organizationId : "0"; }
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final ThreadLocal<TenantContext> tenantContext = ThreadLocal.withInitial(() -> new TenantContext(null, null, null));

    @Configuration
    static class Configurer implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new HttpInterceptor());
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        tenantContext.set(new TenantContext(
                request.getHeader("X-TenantId"), request.getHeader("X-OrganizationId"), request.getHeader("X-Auth-Request-Preferred-Username")));
        
        configureLogsAndTracing(request);

        if (handler instanceof HandlerMethod) {
            log.info(" {} method called for user {} ", ((HandlerMethod) handler).getShortLogMessage(), getUserName());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        tenantContext.remove();
        MDC.remove("tenantId");
    }

    private static void configureLogsAndTracing(HttpServletRequest request) {
        MDC.put("tenantId", getTenantId());
        ServerHttpObservationFilter.findObservationContext(request).ifPresent(
                context -> context.addHighCardinalityKeyValue(KeyValue.of("tenant.id", getTenantId())));
    }

    public static String getTenantId() {
        return tenantContext.get().tenantId != null ? tenantContext.get().tenantId() : "0"; //tdo
    }

    public static String getOrganizationId() {
        return tenantContext.get().organizationId != null ? tenantContext.get().organizationId() : "1"; //tdo
    }

    public static String getUserName() {
        return tenantContext.get().userName != null ? tenantContext.get().userName
                : SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : "";
    }

    /*
    public static void setTenantId(String tenant) {
        tenantId.set(tenant);
    }

     */
}