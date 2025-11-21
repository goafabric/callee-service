package org.goafabric.calleeservice;

import org.jspecify.annotations.Nullable;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.web.client.RestClient;

@SpringBootApplication
//@RegisterReflection(classNames = "org.springdoc.core.providers.SpringWebProvider$$SpringCGLIB$$0", memberCategories = MemberCategory. ACCESS_DECLARED_FIELDS)
@ImportRuntimeHints(Application.AppRuntimeHints.class)
public class Application{

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

    static class AppRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, @Nullable ClassLoader classLoader) {
            hints.reflection().registerType(
                    TypeReference.of("org.springdoc.core.providers.SpringWebProvider$$SpringCGLIB$$0"),
                    builder -> builder.withField("CGLIB$FACTORY_DATA"));

            hints.reflection().registerType(
                    TypeReference.of("org.springdoc.core.providers.SpringWebProvider$$SpringCGLIB$$0"),
                    builder -> builder.withField("CGLIB$CALLBACK_FILTER"));

            hints.reflection().registerType(
                    TypeReference.of("org.springdoc.core.providers.SpringWebProvider"),
                    builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS));
        }
    }
}
