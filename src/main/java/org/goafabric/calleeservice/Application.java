package org.goafabric.calleeservice;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@RegisterReflection(classes = {io.swagger.v3.core.jackson.mixin.Schema31Mixin.TypeSerializer.class, io.swagger.v3.oas.models.media.JsonSchema.class, com.fasterxml.jackson.databind.BeanDescription.class}, memberCategories = {MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_METHODS})
@SpringBootApplication
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner init(ConfigurableApplicationContext context) {
        return args -> {
            if ((args.length > 0) && ("-check-integrity".equals(args[0]))) {
                context.addApplicationListener((ApplicationListener<ApplicationReadyEvent>) event -> {
                    RestClient.create().get().uri("http://localhost:" + context.getEnvironment().getProperty("local.server.port") + "/v3/api-docs").retrieve().body(String.class);
                    SpringApplication.exit(context, () -> 0);
                });
            }
        };
    }

}
