package org.goafabric.calleeservice.crossfunctional

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@Slf4j
class HttpInterceptor : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(object : HandlerInterceptor {
            override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
                tenantId.set(request.getHeader("X-TenantId"))
                userName.set(request.getHeader("X-Auth-Request-Preferred-Username"))
                return true
            }

            override fun afterCompletion(
                request: HttpServletRequest,
                response: HttpServletResponse,
                handler: Any,
                ex: Exception?
            ) {
                tenantId.remove()
                userName.remove()
            }
        })
    }


    companion object {
        private val tenantId = ThreadLocal<String?>()
        private val userName = ThreadLocal<String?>()
        fun getTenantId(): String? {
            return if (tenantId.get() != null) tenantId.get() else "0" //tdo
        }

        fun getUserName(): String? {
            return if (userName.get() != null) userName.get() else if (SecurityContextHolder.getContext().authentication != null) SecurityContextHolder.getContext().authentication.name else ""
        }
    }
}