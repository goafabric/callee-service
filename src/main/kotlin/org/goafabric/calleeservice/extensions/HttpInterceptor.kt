package org.goafabric.calleeservice.extensions

import io.opentelemetry.api.trace.Span
import io.opentelemetry.context.Context
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class HttpInterceptor : HandlerInterceptor {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @Configuration
    internal class Configurer : WebMvcConfigurer {
        @Value("\${cors.enabled:false}")
        private val corsEnabled = false

        override fun addInterceptors(registry: InterceptorRegistry) {
            registry.addInterceptor(HttpInterceptor())
        }

        override fun addCorsMappings(registry: CorsRegistry) {
            if (!corsEnabled) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*")
            }
        }
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        UserContext.setContext(request)
        configureLogsAndTracing()

        if (handler is HandlerMethod) {
            log.info(" {} method called for user {} ", handler.shortLogMessage, UserContext.userName)
        }
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
       afterCompletion()
    }

    private fun configureLogsAndTracing() {
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.tenantId)
        MDC.put("tenantId", UserContext.tenantId)
    }

    private fun afterCompletion() {
        UserContext.removeContext()
        MDC.remove("tenantId")
    }

}