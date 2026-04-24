package org.goafabric.calleeservice.controller;

import org.goafabric.calleeservice.logic.CalleeLogic;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "callees", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalleeController {
    private final CalleeLogic calleeLogic;

    public CalleeController(CalleeLogic calleeLogic) {
        this.calleeLogic = calleeLogic;
    }

    //simplest way, let spring to the Async handling and just wrap the result as CompleteableFuture
    //important!: this works as long as we call a simple Future, when we return CompletableFuture Spring will BLOCK towards the caller
    @Async
    @GetMapping("sayMyName")
    public Future<Callee> sayMyName (@RequestParam("name") String name) {
       return CompletableFuture.completedFuture(calleeLogic.sayMyName(name));
    }

    //manual way, if we really want to do the async call ourselves and return a value
    //important: only Controller nows of Threading, lower level have no knowledge here
    @GetMapping("sayMyOtherName/{name}")
    public String sayMyOtherName(@PathVariable("name") String name) {
        String jobId = "4711";
        CompletableFuture.runAsync(() -> calleeLogic.sayMyOtherName(name)); // fire and forget, supplyAsync can help you to return the real value
        return jobId; //return an id for polling, you should rather opt for sending a websocket event here
    }

    @PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Callee save(@RequestBody Callee callee) {
        return calleeLogic.save(callee);
    }

}

