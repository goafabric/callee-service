package org.goafabric.calleeservice.crossfunctional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SecurityConfiguration {
    @Value("${security.authentication.enabled}")
    private Boolean isAuthenticationEnabled;

    @Value("${spring.security.oauth2.client.provider.keycloak.auth-base-uri:}")
    private String authBaseUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (isAuthenticationEnabled) {
            http
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/" ,"/actuator/**", "/welcome/**").permitAll()
                            .anyRequest().authenticated())
                    .oauth2Login(Customizer.withDefaults())
                    .oauth2ResourceServer().authenticationManagerResolver(new JwtIssuerAuthenticationManagerResolver(new MyJwtAuthenticationManagerResolver(authBaseUri)));
        }
        return http.build();
    }

    //Multitenant Resourceserver for Backend2Backend by just comparing the baseUri and skipping the original auth
    @Slf4j
    @RequiredArgsConstructor
    private static class MyJwtAuthenticationManagerResolver implements AuthenticationManagerResolver<String> {
        private final String authBaseUri;
        private final Map<String, AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();

        @Override
        public AuthenticationManager resolve(String issuer) {
            if (true) {
            //if (issuer.startsWith(authBaseUri)) {
                return this.authenticationManagers.computeIfAbsent(issuer,
                        (k) -> new JwtAuthenticationProvider(JwtDecoders.fromIssuerLocation(issuer))::authenticate);
            }
            return null;
        }
    }
}
