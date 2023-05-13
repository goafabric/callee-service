package org.goafabric.calleeservice.crossfunctional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class AuthoritiesMapper {
    @Bean
    GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            final Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            authorities.forEach(authority -> {
                mappedAuthorities.add(authority); //add original authority if needed
                if (authority instanceof OidcUserAuthority oidcAuth) {
                    List<String> roles = (List<String>) oidcAuth.getAttributes().get("roles");
                    //add role from oidc attributes so that f*** spring can understand it and also add f*** ROLE_ prefix for f*** spring
                    roles.forEach(role -> mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                }
            });

            return mappedAuthorities;
        };
    }

}
