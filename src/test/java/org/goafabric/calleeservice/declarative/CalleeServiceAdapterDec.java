package org.goafabric.calleeservice.declarative;

import org.goafabric.calleeservice.controller.Callee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface CalleeServiceAdapterDec {

    @GetExchange("/callees/sayMyName")
    Callee sayMyName (@RequestParam String name);

    @GetExchange("sayMyOtherName/{name}")
    Callee sayMyOtherName(@PathVariable String name);

}
