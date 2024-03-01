package org.goafabric.calleeservice.extensions;

import io.micrometer.common.KeyValue;
import jakarta.annotation.security.RolesAllowed;
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

import java.util.Arrays;
import java.util.List;


//add to existing HttpInterceptor
public class HttpInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        tenantId.set(request.getHeader("X-TenantId"));
        userName.set(request.getHeader("X-Auth-Request-Preferred-Username"));
        configureLogsAndTracing(request);

        if (handler instanceof HandlerMethod) {
            log.info(" {} method called for user {} ", ((HandlerMethod) handler).getShortLogMessage(), getUserName());
            checkRoles((HandlerMethod) handler, getUserName());
        }
        return true;
    }

    private static void checkRoles(HandlerMethod handler, String userName) {
        List<String> methodRoles = Arrays.asList(handler.getMethodAnnotation(RolesAllowed.class).value());
        List<String> userRoles = Arrays.asList("READ", "WRITE", "ADDRESS_DELETE"); //TODO: getBackendRoles(userName) => read roles via DB or REST with every call if not cached
        methodRoles.stream().filter(role -> !userRoles.contains(role)).findAny().ifPresent(role -> {
            throw new IllegalStateException("not allowed");
        });
    }
}

//Example implementation
public class SomethinController() {
    @RolesAllowed("READ")
    public void getById(String id) {}

    @RolesAllowed("DELETE")
    public void deleteById(String id) {}
}