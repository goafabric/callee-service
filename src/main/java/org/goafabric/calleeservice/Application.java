package org.goafabric.calleeservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


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

    /*
    @Configuration
    static class SecurityConfiguration  {
        @Value("${security.authentication.enabled:true}")
        private Boolean isAuthenticationEnabled;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            if (isAuthenticationEnabled) { http.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable(); }
            else { http.authorizeRequests().anyRequest().permitAll(); }
            return http.build();
        }
    }
     */
}
