package org.goafabric.calleeservice;

import org.goafabric.calleeservice.aspect.TestAspect;
import org.goafabric.calleeservice.aspect.TestComponent;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;


/**
 * Created by amautsch on 26.06.2015.
 */

@SpringBootApplication
@ImportRuntimeHints({Application.ApplicationRuntimeHints.class})
public class Application {

    public static void main(String[] args) throws Exception {
        var context = SpringApplication.run(Application.class, args);

        var clazz = Class.forName("org.goafabric.calleeservice.Callee");
        System.err.println("Testing reflection : " + clazz.getMethod("getMessage").invoke(clazz.getDeclaredConstructor().newInstance()));
        System.err.println("Testing file read : " + new String(new ClassPathResource("secret/secret.txt").getInputStream().readAllBytes()));

        context.getBean(TestComponent.class).callOnMe();
        context.getBean(TestComponent.class).callOnMe(); //when @Cachabele annotated only gets called once

        try { Thread.currentThread().join(10000);} catch (InterruptedException e) {}
    }

    @Bean
    public CommandLineRunner init(ApplicationContext context, TestComponent testComponent) {
        return args -> {if ((args.length > 0) && ("-check-integrity".equals(args[0]))) {SpringApplication.exit(context, () -> 0);}};
    }

    static class ApplicationRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(org.goafabric.calleeservice.Callee.class,
                    MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

            hints.resources().registerPattern("secret/*");

            hints.reflection().registerType(TestAspect.class, MemberCategory.INVOKE_DECLARED_METHODS);
        }
    }

}
