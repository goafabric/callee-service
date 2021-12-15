package org.goafabric.calleeservice.crossfunctional;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.nativex.hint.ProxyBits;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This Aspect will be invoked around every method that is part of a {@link org.springframework.web.bind.annotation.RestController} annotated class. It will log the method's signature and duration of the call.
 */
@Component
@Aspect
@Slf4j
@AotProxyHint(targetClass = org.goafabric.calleeservice.logic.CalleeLogic.class, proxyFeatures = ProxyBits.IS_STATIC)
public class DurationLogAspect {

    @Around("execution(public * org.goafabric.calleeservice.logic.CalleeLogic.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        final long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            log.info("{} took {}ms for user {}", toString(method), System.currentTimeMillis() - startTime, getUserName());
        }
    }

    private String toString(final Method method) {
        final String parameterTypes = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(","));
        return String.format("%s.%s(%s)", method.getDeclaringClass().getSimpleName(),
                method.getName(), parameterTypes);
    }

    private String getUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication == null) ? "" : authentication.getName();
    }
}
