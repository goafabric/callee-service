package org.goafabric.calleeservice;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;


/**
 * Created by amautsch on 26.06.2015.
 */

@SpringBootApplication
@ImportRuntimeHints({Application.ApplicationRuntimeHints.class})
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);

        System.err.println("Hello from Spring Boot");

        try { doReflection(); } catch (Exception e ) {}

        try { Thread.currentThread().join(10000);} catch (InterruptedException e) {}
    }

    private static void doReflection() throws  Exception {
        final Class clazz = Class.forName("org.goafabric.calleeservice.Callee");
        System.err.println(clazz.getMethod("getMessage").invoke(
                clazz.getDeclaredConstructor().newInstance()));
    }

    static class ApplicationRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(org.goafabric.calleeservice.Callee.class,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_DECLARED_METHODS);
        }
    }

}
