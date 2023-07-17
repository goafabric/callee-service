package org.goafabric.calleeservice.crossfunctional

import io.micrometer.common.KeyValue
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.observation.ServerRequestObservationContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.ServerHttpObservationFilter
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

class HttpInterceptor : HandlerInterceptor {
    private val log = LoggerFactory.getLogger(this.javaClass.name)

    @Configuration
    internal class Configurer : WebMvcConfigurer {
        override fun addInterceptors(registry: InterceptorRegistry) {
            registry.addInterceptor(HttpInterceptor())
        }
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        tenantId.set(request.getHeader("X-TenantId"))
        userName.set(request.getHeader("X-Auth-Request-Preferred-Username"))
        configureLogsAndTracing(request)
        if (handler is HandlerMethod) {
            log.info(" {} method called for user {} ", handler.shortLogMessage, getUserName())
        }
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        tenantId.remove()
        MDC.remove("tenantId")
    }

    companion object {
        private val tenantId = ThreadLocal<String?>()
        private val userName = ThreadLocal<String?>()
        private fun configureLogsAndTracing(request: HttpServletRequest) {
            MDC.put("tenantId", getTenantId())
            ServerHttpObservationFilter.findObservationContext(request)
                .ifPresent { context: ServerRequestObservationContext ->
                    context.addHighCardinalityKeyValue(
                        KeyValue.of("tenant.id", getTenantId()!!)
                    )
                }
        }

        fun getTenantId(): String? {
            return if (tenantId.get() != null) tenantId.get() else "0" //tdo
        }

        fun getUserName(): String? {
            return if (userName.get() != null) userName.get() else if (SecurityContextHolder.getContext().authentication != null) SecurityContextHolder.getContext().authentication.name else ""
        }

        fun setTenantId(tenant: String?) {
            tenantId.set(tenant)
        }
    }
}