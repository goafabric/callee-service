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
    private static final ThreadLocal<String> userName = new ThreadLocal<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                tenantId.set(request.getHeader("X-TenantId"));
                userName.set(request.getHeader("X-Auth-Request-Preferred-Username"));
                configureLogsAndTracing(request, tenantId.get());
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                tenantId.remove();
                userName.remove();
                MDC.remove("tenantId");
            }

            private static void configureLogsAndTracing(HttpServletRequest request, String tenantId) {
                if (tenantId != null) {
                    MDC.put("tenantId", tenantId);
                    ServerHttpObservationFilter.findObservationContext(request).ifPresent(context -> context.addHighCardinalityKeyValue(KeyValue.of("tenant.id", tenantId)));
                }
            }
        });
    }

    public static String getTenantId() {
        return tenantId.get() != null ? tenantId.get() : "0"; //tdo
    }

    public static String getUserName() {
        return userName.get() != null ? userName.get()
                : SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : "";
    }

}