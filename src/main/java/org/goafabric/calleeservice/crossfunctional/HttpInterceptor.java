package org.goafabric.calleeservice.crossfunctional;

import io.micrometer.observation.ObservationPredicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
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
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                tenantId.remove();
                userName.remove();
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, @Value("${security.authentication.enabled:true}") Boolean isAuthenticationEnabled) throws Exception {
        return isAuthenticationEnabled ? http.authorizeHttpRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable().build()
                                        : http.authorizeHttpRequests().anyRequest().permitAll().and().build();
    }
    
    @Bean
    ObservationPredicate disableHttpServerObservationsFromName() { return (name, context) -> !name.startsWith("spring.security."); }

}