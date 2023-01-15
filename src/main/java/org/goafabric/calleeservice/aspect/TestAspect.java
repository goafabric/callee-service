package org.goafabric.calleeservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Aspect
public class TestAspect {

    //@Around("execution(public * org.goafabric.calleeservice.aspect.TestComponent.*(..))")
    //@Around("execution(public * org.goafabric.calleeservice.aspect.*.*(..))")
    @Around("@within(org.goafabric.calleeservice.aspect.TestAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } finally {
            final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            System.err.println("Testing aspect wrapping around : " + toString(method));
        }
    }

    private String toString(final Method method) {
        final String parameterTypes = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName).collect(Collectors.joining(","));
        return String.format("%s.%s(%s)", method.getDeclaringClass().getSimpleName(),
                method.getName(), parameterTypes);
    }

}
