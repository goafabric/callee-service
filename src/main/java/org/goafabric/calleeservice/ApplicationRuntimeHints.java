package org.goafabric.calleeservice;

import org.jspecify.annotations.Nullable;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(ApplicationRuntimeHints.class)
public class ApplicationRuntimeHints implements RuntimeHintsRegistrar {

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
