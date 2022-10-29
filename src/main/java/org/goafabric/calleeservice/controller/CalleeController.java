package org.goafabric.calleeservice.controller;

import org.goafabric.calleeservice.logic.CalleeLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "callees", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalleeController {
    @Autowired
    CalleeLogic calleeLogic;

    @GetMapping("sayMyName")
    public Callee sayMyName (@RequestParam String name) {
        return calleeLogic.sayMyName(name);
    }

    @GetMapping("sayMyOtherName/{name}")
    public Callee sayMyOtherName(@PathVariable String name) {
        return calleeLogic.sayMyOtherName(name);
    }

    @PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callee save(@RequestBody Callee callee) {
        return calleeLogic.save(callee);
    }

}

