package org.goafabric.calleeservice.crossfunctional

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import java.util.*
import java.util.stream.Collectors

/**
 * This Aspect will be invoked around every method that is part of a [org.springframework.web.bind.annotation.RestController] annotated class. It will log the method's signature and duration of the call.
 */
@Component
@Aspect
@ImportRuntimeHints(DurationLogger.ApplicationRuntimeHints::class)
class DurationLogger {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Around("execution(public * org.goafabric.calleeservice.logic.CalleeLogic.*(..))")
    //@Around("@within(org.goafabric.calleeservice.crossfunctional.DurationLog)")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint): Any {
        val startTime = System.currentTimeMillis()
        return try {
            joinPoint.proceed()
        } finally {
            val method = (joinPoint.signature as MethodSignature).method
            log.info("{} took {}ms for user: {} , tenant: {}", toString(method),
                System.currentTimeMillis() - startTime, HttpInterceptor.getUserName(), HttpInterceptor.getTenantId())
        }
    }

    private fun toString(method: Method): String {
        val parameterTypes = Arrays.stream(method.parameterTypes)
            .map { obj: Class<*> -> obj.simpleName }.collect(Collectors.joining(","))
        return String.format("%s.%s(%s)", method.declaringClass.simpleName,
            method.name, parameterTypes
        )
    }

    internal class ApplicationRuntimeHints : RuntimeHintsRegistrar {
        override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
            hints.reflection().registerType(DurationLogger::class.java, MemberCategory.INVOKE_DECLARED_METHODS)
        }
    }
}