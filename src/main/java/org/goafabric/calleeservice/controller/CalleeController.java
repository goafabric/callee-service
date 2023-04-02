package org.goafabric.calleeservice.controller;

import org.goafabric.calleeservice.logic.CalleeLogic;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "callees", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalleeController {
    private final CalleeLogic calleeLogic;

    public CalleeController(CalleeLogic calleeLogic) {
        this.calleeLogic = calleeLogic;
    }

    @GetMapping("sayMyName")
    public Callee sayMyName (@RequestParam("name") String name) {
        return calleeLogic.sayMyName(name);
    }

    @GetMapping("sayMyOtherName/{name}")
    public Callee sayMyOtherName(@PathVariable("name") String name) {
        return calleeLogic.sayMyOtherName(name);
    }

    @PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callee save(@RequestBody Callee callee) {
        return calleeLogic.save(callee);
    }

}

