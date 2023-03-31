package org.goafabric.calleeservice.declarative;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.goafabric.calleeservice.controller.Callee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

@CircuitBreaker(name = "calleeservice")
public interface CalleeServiceAdapterDec {

    @GetExchange("/callees/sayMyName")
    Callee sayMyName (@RequestParam("name") String name);

    @GetExchange("/callees/sayMyOtherName/{name}")
    Callee sayMyOtherName(@PathVariable("name") String name);

}
