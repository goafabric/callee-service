package org.goafabric.calleeservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


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

    @Configuration
    static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Value("${security.authentication.enabled:true}")
        private Boolean isAuthenticationEnabled;

        @Override
        protected void configure(final HttpSecurity httpSecurity) throws Exception {
            if (isAuthenticationEnabled) { httpSecurity.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable(); }
            else { httpSecurity.authorizeRequests().anyRequest().permitAll(); }
        }
    }
}
