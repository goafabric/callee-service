package org.goafabric.calleeservice;

import io.micrometer.observation.ObservationPredicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Created by amautsch on 26.06.2015.
 */

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
    @ConditionalOnMissingClass("org.springframework.security.oauth2.client.OAuth2AuthorizationContext")
    public SecurityFilterChain filterChain(HttpSecurity http, @Value("${security.authentication.enabled:true}") Boolean isAuthenticationEnabled) throws Exception {
        return isAuthenticationEnabled
                ? http.authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/**").permitAll().anyRequest().authenticated())
                    .httpBasic(httpBasic -> {}).csrf(csrf -> csrf.disable()).build()
                : http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).build();
    }

    @Bean
    ObservationPredicate disableHttpServerObservationsFromName() { return (name, context) -> !name.startsWith("spring.security."); }

}
