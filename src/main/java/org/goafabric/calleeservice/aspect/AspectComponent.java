package org.goafabric.calleeservice.aspect;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;

@Component
@ImportRuntimeHints(AspectComponent.AspectRuntimeHints.class)
public class AspectComponent {
    public void run(ConfigurableApplicationContext context) {
        System.err.println("\nTesting Aspect stuff");
        if (context.isActive()) {
            doAspect(context.getBean(TestComponent.class));
            doCache(context.getBean(TestComponent.class));
        }
    }

    private void doAspect(TestComponent testComponent) {
        testComponent.callOnMe("0");
    }

    private void doCache(TestComponent testComponent) {
        testComponent.callOnMe("0"); //when @Cacheable annotated only gets called once

        testComponent.getBar("1");
        testComponent.getFoo("1"); //will throw classcast without KeyGenerator
    }

    static class AspectRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            //hints.reflection().registerType(TestAspect.class, MemberCategory.INVOKE_DECLARED_METHODS);
        }
    }

}
