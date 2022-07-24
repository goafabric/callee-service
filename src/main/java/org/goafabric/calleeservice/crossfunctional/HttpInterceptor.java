package org.goafabric.calleeservice.crossfunctional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class HttpInterceptor implements WebMvcConfigurer {
    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();
    private static final ThreadLocal<String> userName = new ThreadLocal<>();

    public static String getTenantId() { return tenantId.get(); }
    public static String getUserName() { return userName.get(); }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                tenantId.set(request.getHeader("X-TenantId") != null ? request.getHeader("X-TenantId") : "0"); //TODO
                userName.set(request.getHeader("X-Auth-Request-Preferred-Username") != null ? request.getHeader("X-Auth-Request-Preferred-Username")
                                                :  SecurityContextHolder.getContext().getAuthentication().getName());
                //log.info("tenantId: {}", tenantId.get());
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                tenantId.remove();
                userName.remove();
            }
        });
    }

    @Value("${security.authentication.enabled:true}")
    private Boolean isAuthenticationEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (isAuthenticationEnabled) { http.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable(); }
        else { http.authorizeRequests().anyRequest().permitAll(); }
        return http.build();
    }
}