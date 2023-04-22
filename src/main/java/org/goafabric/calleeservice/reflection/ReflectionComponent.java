package org.goafabric.calleeservice.reflection;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Component
@ImportRuntimeHints(ReflectionComponent.ReflectionRuntimeHints.class)
public class ReflectionComponent {
    public void run() throws Exception {
        reflectMethod();
        readClassPathResource();
    }

    private static void reflectMethod() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        var clazz = Class.forName("org.goafabric.calleeservice.reflection.Callee");
        System.err.println("Testing reflection : " + clazz.getMethod("getMessage").invoke(clazz.getDeclaredConstructor().newInstance()));
    }


    private static void readClassPathResource() throws IOException {
        System.err.println("Testing file read : " + new String(new ClassPathResource("secret/secret.txt").getInputStream().readAllBytes()));
    }


    static class ReflectionRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(Callee.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.resources().registerPattern("secret/*");
        }
    }

}
