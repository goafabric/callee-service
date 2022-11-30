package org.goafabric.calleeservice.crossfunctional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
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
        if (isAuthenticationEnabled) { http.authorizeHttpRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable(); }
        else { http.authorizeHttpRequests().anyRequest().permitAll(); }
        return http.build();
    }
    
    /*
    @Bean
    ObservationPredicate disableHttpServerObservationsFromName() {
        return (name, context) -> !name.startsWith("spring.security.");
    }
    */

    @Component
    class FilterChainProxyPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
            if (bean instanceof FilterChainProxy) {
                ((FilterChainProxy) bean).setFilterChainDecorator(new FilterChainProxy.VirtualFilterChainDecorator());
            }
            return bean;
        }
    }

}