package org.goafabric.calleeservice;

import io.micrometer.observation.ObservationPredicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@SpringBootApplication
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner init(ApplicationContext context) {
        return args -> {if ((args.length > 0) && ("-check-integrity".equals(args[0]))) {SpringApplication.exit(context, () -> 0);}};
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, @Value("${security.authentication.enabled:true}") boolean isAuthenticationEnabled, HandlerMappingIntrospector introspector) throws Exception {
        return isAuthenticationEnabled
                ? http.authorizeHttpRequests(auth -> auth.requestMatchers(new MvcRequestMatcher(introspector, "/actuator/**")).permitAll().anyRequest().authenticated())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .httpBasic(httpBasic -> {}).csrf(AbstractHttpConfigurer::disable).build()
                : http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).httpBasic(httpBasic -> {}).csrf(AbstractHttpConfigurer::disable).build();
    }

    @Bean
    ObservationPredicate disableHttpServerObservationsFromName() { return (name, context) -> !(name.startsWith("spring.security.") || (context instanceof ServerRequestObservationContext serverContext && (serverContext).getCarrier().getRequestURI().startsWith("/actuator"))); }

}
