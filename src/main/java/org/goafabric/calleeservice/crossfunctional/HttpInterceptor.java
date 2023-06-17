package org.goafabric.calleeservice.crossfunctional;

import io.micrometer.common.KeyValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.ServerHttpObservationFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class HttpInterceptor implements WebMvcConfigurer {
    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                setTenantId(request.getHeader("X-TenantId"), request);
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                tenantId.remove();
                MDC.remove("tenantId");
            }

        });
    }

    private static void setTenantId(String tenant, HttpServletRequest request) {
        tenantId.set(tenant);
        MDC.put("tenantId", getTenantId());
        ServerHttpObservationFilter.findObservationContext(request).ifPresent(
                context -> context.addHighCardinalityKeyValue(KeyValue.of("tenant.id", getTenantId())));
    }

    public static String getTenantId() {
        return tenantId.get() != null ? tenantId.get() : "0"; //tdo
    }

    public static String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : "";
    }

}