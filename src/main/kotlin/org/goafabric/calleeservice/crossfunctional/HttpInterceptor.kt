package org.goafabric.calleeservice.crossfunctional

import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationPredicate
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class HttpInterceptor : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(object : HandlerInterceptor {
            override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
                tenantId.set(request.getHeader("X-TenantId"))
                userName.set(request.getHeader("X-Auth-Request-Preferred-Username"))
                return true
            }

            override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
                tenantId.remove()
                userName.remove()
            }
        })
    }

    companion object {
        private val tenantId = ThreadLocal<String>()
        private val userName = ThreadLocal<String>()
        fun getTenantId(): String {
            return if (tenantId.get() != null) tenantId.get() else "0" //tdo
        }

        fun getUserName(): String {
            return if (userName.get() != null) userName.get() else if (SecurityContextHolder.getContext().authentication != null) SecurityContextHolder.getContext().authentication.name else ""
        }
    }


    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity, @Value("\${security.authentication.enabled:true}") isAuthenticationEnabled: Boolean): SecurityFilterChain {
        return if (isAuthenticationEnabled) http.authorizeHttpRequests().anyRequest().authenticated().and().httpBasic()
            .and().csrf().disable().build() else http.authorizeHttpRequests().anyRequest().permitAll().and().build()
    }

    @Bean
    fun disableHttpServerObservationsFromName(): ObservationPredicate {
        return ObservationPredicate { name: String, context: Observation.Context? -> !name.startsWith("spring.security.") }
    }
}