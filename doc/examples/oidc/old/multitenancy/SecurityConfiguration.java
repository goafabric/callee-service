package org.goafabric.calleeservice.crossfunctional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfiguration  {
    @Value("${security.authentication.enabled}")
    private Boolean authEnabled;

    @Value("${spring.security.auth-server-url}")
    private String authBaseUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (authEnabled) {
            http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/" ,"/actuator/**", "/welcome/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer().authenticationManagerResolver(new JwtIssuerAuthenticationManagerResolver(
                        new SecurityLoginConfiguration.MyJwtAuthenticationManagerResolver(authBaseUri)));

        } else {
            http.authorizeRequests(authorize -> authorize.anyRequest().permitAll());
        }

        http.addFilterBefore(new HttpInterceptor(), SecurityContextPersistenceFilter.class);
        addTenantSwitchingReloginFilterWorkaround(http);
        return http.build();
    }

    private void addTenantSwitchingReloginFilterWorkaround(HttpSecurity http) {
        http.addFilterAfter((servletRequest, servletResponse, filterChain) -> {
            filterChain.doFilter(servletRequest, servletResponse);
            new SecurityContextLogoutHandler().logout((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, SecurityContextHolder.getContext().getAuthentication());
        }, FilterSecurityInterceptor.class);
    }

}

