package org.goafabric.calleeservice.adapter

import org.goafabric.calleeservice.controller.dto.Callee
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class CalleeServiceAdapter(
    private val restTemplate: RestTemplate) {

    @Value("\${adapter.calleeservice.url}")
    private val url: String? = null

    fun sayMyName(name: String): Callee? {
        return restTemplate.getForObject("$url/callees/sayMyName?name={name}", Callee::class.java, name)
    }

    fun sayMyOtherName(name: String): Callee? {
        return restTemplate.getForObject("$url/callees/sayMyOtherName/{name}", Callee::class.java, name)
    }

}