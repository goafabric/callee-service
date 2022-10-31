package org.goafabric.calleeservice;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;


/**
 * Created by amautsch on 26.06.2015.
 */

@SpringBootApplication
@ImportRuntimeHints({Application.ApplicationRuntimeHints.class})
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);

        System.err.println("Hello from Spring Boot");

        doReflection();
        readFile();

        try { Thread.currentThread().join(5000);} catch (InterruptedException e) {}
    }



    private static void doReflection() {
        final Class clazz;
        try {
            clazz = Class.forName("org.goafabric.calleeservice.Callee");
            System.err.println(clazz.getMethod("getMessage").invoke(
                    clazz.getDeclaredConstructor().newInstance()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readFile() {
        try {
            System.err.println("Reading file ...");
            return new ClassPathResource("secret/secret.txt").getInputStream().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class ApplicationRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(org.goafabric.calleeservice.Callee.class,
                    MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

            hints.resources().registerPattern("secret/*");
        }
    }

}
