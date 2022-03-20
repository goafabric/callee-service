
package org.goafabric.calleeservice.crossfunctional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration//(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${security.authentication.enabled:true}")
    private Boolean isAuthenticationEnabled;

    @Override //in memory authentication
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin")
                .password("$2a$10$X3nRHu5n2X0Ibny6fWJJvOFbdmrbZOmkTZbWv9001G8HY6LEPxWei")
                .roles("standard_role");
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        if (isAuthenticationEnabled) {
            httpSecurity.authorizeRequests()
                    .antMatchers("/actuator/**").permitAll()
                    .anyRequest().authenticated()
                    .and().httpBasic()
                    .and().csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        } else {
            httpSecurity.authorizeRequests().anyRequest().permitAll();
        }
    }

    static String getUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication == null) ? "" : authentication.getName();
    }
}
