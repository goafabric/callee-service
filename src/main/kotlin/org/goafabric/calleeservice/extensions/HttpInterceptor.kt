package org.goafabric.calleeservice.extensions

import io.micrometer.common.KeyValue
import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationPredicate
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.observation.ServerRequestObservationContext
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.filter.ServerHttpObservationFilter
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
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

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        TenantContext.removeContext()
        MDC.remove("tenantId")
    }

    private fun configureLogsAndTracing(request: HttpServletRequest) {
        MDC.put("tenantId", TenantContext.tenantId)
        ServerHttpObservationFilter.findObservationContext(request)
            .ifPresent { context: ServerRequestObservationContext ->
                context.addHighCardinalityKeyValue(
                    KeyValue.of("tenant.id", TenantContext.tenantId)
                )
            }
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity, @Value("\${security.authentication.enabled:true}") isAuthenticationEnabled: Boolean): SecurityFilterChain? {
        return if (isAuthenticationEnabled) http.authorizeHttpRequests { auth ->
            auth.requestMatchers("/actuator/**").permitAll().anyRequest().authenticated()
        }
            .httpBasic { }
            .csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
            .build()
        else http.authorizeHttpRequests { auth -> auth.anyRequest().permitAll() }.build()
    }

    @Bean @SuppressWarnings("java:S1874")
    fun passwordEncoder(): PasswordEncoder? { return NoOpPasswordEncoder.getInstance() }

    @Bean
    fun disableHttpServerObservationsFromName(): ObservationPredicate? {
        return ObservationPredicate { name: String, context: Observation.Context? -> !name.startsWith("spring.security.") || (context is ServerRequestObservationContext  && (context).carrier.requestURI.startsWith("/actuator")) }
    }
}