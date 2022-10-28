package org.goafabric.calleeservice.crossfunctional

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
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
@Slf4j
class HttpInterceptor : WebMvcConfigurer {
    companion object {
        private val tenantId = ThreadLocal<String>()
        private val userName = ThreadLocal<String>()

        fun getTenantId(): String? { return tenantId.get() }
        fun getUserName(): String? { return userName.get() }
    }
    
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(object : HandlerInterceptor {
            override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
                tenantId.set(if (request.getHeader("X-TenantId") != null) request.getHeader("X-TenantId") else "0") //TODO
                userName.set(if (request.getHeader("X-Auth-Request-Preferred-Username") != null) request.getHeader("X-Auth-Request-Preferred-Username") else SecurityContextHolder.getContext().authentication.name)
                return true
            }

            override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
                tenantId.remove()
                userName.remove()
            }
        })
    }


    @Value("\${security.authentication.enabled}")
    private val isAuthenticationEnabled: Boolean = true

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        if (isAuthenticationEnabled) {
            http.authorizeHttpRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable()
        } else {
            http.authorizeHttpRequests().anyRequest().permitAll()
        }
        return http.build()
    }
}