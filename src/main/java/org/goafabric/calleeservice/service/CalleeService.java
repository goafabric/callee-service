package org.goafabric.calleeservice.service;

import org.goafabric.calleeservice.logic.CalleeLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "callees", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalleeService {
    @Autowired
    CalleeLogic calleeLogic;

    @GetMapping("isAlive")
    public Boolean isAlive() {
        return calleeLogic.isAlive();
    }

    @GetMapping("setSleepTime")
    public String setSleepTime(@RequestParam Long sleepTime) {
        return calleeLogic.setSleepTime(sleepTime);
    }

    @GetMapping("sayMyName")
    public String sayMyName (@RequestParam String name) {
        return calleeLogic.sayMyName(name);
    }

    @GetMapping("sayMyOtherName/{name}")
    String sayMyOtherName(@PathVariable String name) {
        return calleeLogic.sayMyOtherName(name);
    }

}

