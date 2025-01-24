package org.goafabric.calleeservice.remote

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.goafabric.calleeservice.controller.dto.Callee
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

@CircuitBreaker(name = "calleeservice")
interface CalleeControllerRemote {
    @GetExchange("/callees/sayMyName")
    fun sayMyName(@RequestParam("name") name: String?): Callee

    @GetExchange("/callees/sayMyOtherName/{name}")
    fun sayMyOtherName(@PathVariable("name") name: String?): Callee
}
