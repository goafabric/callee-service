package org.goafabric.calleeservice.crossfunctional;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
@ImportRuntimeHints(DurationLogger.ApplicationRuntimeHints.class)
public class DurationLogger {
    //private static Logger log = LoggerFactory.getLogger(DurationLogger.class);

    @Around("@within(org.goafabric.calleeservice.crossfunctional.DurationLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        final long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            log.info("{} took {}ms for user: {} , tenant: {}", toString(method), System.currentTimeMillis() - startTime, HttpInterceptor.getUserName(), HttpInterceptor.getTenantId());
        }
    }

    private String toString(final Method method) {
        final String parameterTypes = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName).collect(Collectors.joining(","));
        return String.format("%s.%s(%s)", method.getDeclaringClass().getSimpleName(),
                method.getName(), parameterTypes);
    }

    static class ApplicationRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(DurationLogger.class, MemberCategory.INVOKE_DECLARED_METHODS);
        }
    }

}
