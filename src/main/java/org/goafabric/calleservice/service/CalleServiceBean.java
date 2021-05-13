package org.goafabric.calleservice.service;

import org.goafabric.calleservice.logic.CalleLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "callees",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CalleServiceBean {
    @Autowired
    private CalleLogic calleLogic;

    @GetMapping("isAlive")
    public Boolean isAlive() {
        return calleLogic.isAlive();
    }

    @GetMapping("setSleepTime")
    public String setSleepTime(@RequestParam Long sleepTime) {
        return calleLogic.setSleepTime(sleepTime);
    }

    @GetMapping("sayMyName")
    public String sayMyName (@RequestParam String name) {
        return calleLogic.sayMyName(name);
    }

    @GetMapping("sayMyOtherName/{name}")
    String sayMyOtherName(@PathVariable String name) {
        return calleLogic.sayMyOtherName(name);
    }

}

