package org.goafabric.calleeservice.extensions;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
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
            if (!corsEnabled) { registry.addMapping("/**").allowedOrigins("*")
                    .allowedMethods("GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS", "HEAD"); }
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserContext.setContext(request);
        configureLogsAndTracing();

        if (handler instanceof HandlerMethod handlerMethod) {
            log.info(" {} method called for user {} ", handlerMethod.getShortLogMessage(), UserContext.getUserName());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        afterCompletion();
    }

    private static void configureLogsAndTracing() {
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.getTenantId());
        MDC.put("tenantId", UserContext.getTenantId());
    }

    private static void afterCompletion() {
        UserContext.removeContext();
        MDC.remove("tenantId");
    }

    @RegisterReflectionForBinding(HttpInterceptor.class)
    public String getTenantPrefix() { return UserContext.getTenantId() + "_"; }
}