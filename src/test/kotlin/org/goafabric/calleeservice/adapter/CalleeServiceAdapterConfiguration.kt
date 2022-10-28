package org.goafabric.calleeservice.adapter

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.util.*

@Configuration
class CalleeServiceAdapterConfiguration {
    @Bean
    fun restTemplate(
        @Value("\${adapter.calleeservice.user}") user: String?,
        @Value("\${adapter.calleeservice.password}") password: String?,
        @Value("\${adapter.timeout}") timeout: Int
    ): RestTemplate {
        val restTemplate = RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(timeout.toLong()))
            .setReadTimeout(Duration.ofMillis(timeout.toLong()))
            .build()
        restTemplate.messageConverters = listOf<HttpMessageConverter<*>>(MappingJackson2HttpMessageConverter())
        restTemplate.interceptors.add(ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution ->
            request.headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE
            request.headers.setBasicAuth(
                String(Base64.getDecoder().decode(user)),
                String(Base64.getDecoder().decode(password))
            )
            execution.execute(request, body)
        })
        return restTemplate
    }
}