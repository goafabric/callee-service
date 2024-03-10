package org.goafabric.calleeservice.extensions

import io.micrometer.common.KeyValue
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.observation.ServerRequestObservationContext
import org.springframework.web.filter.ServerHttpObservationFilter
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

class HttpInterceptor : HandlerInterceptor {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @Configuration
    internal class Configurer : WebMvcConfigurer {
        override fun addInterceptors(registry: InterceptorRegistry) {
            registry.addInterceptor(HttpInterceptor())
        }
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        TenantContext.setContext(request)
        configureLogsAndTracing(request)

        if (handler is HandlerMethod) {
            log.info(" {} method called for user {} ", handler.shortLogMessage, TenantContext.userName)
        }
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        TenantContext.removeContext()
        MDC.remove("tenantId")
    }

    companion object {
        private fun configureLogsAndTracing(request: HttpServletRequest) {
            MDC.put("tenantId", TenantContext.tenantId)
            ServerHttpObservationFilter.findObservationContext(request)
                .ifPresent { context: ServerRequestObservationContext ->
                    context.addHighCardinalityKeyValue(
                        KeyValue.of("tenant.id", TenantContext.tenantId)
                    )
                }
        }
    }
}