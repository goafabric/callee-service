package org.goafabric.calleservice.service;

import org.goafabric.calleservice.logic.CalleeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = CalleService.RESOURCE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CalleServiceBean implements CalleService {
    @Autowired
    private CalleeService calleeService;

    @Override
    @GetMapping("isAlive")
    public Boolean isAlive() {
        return calleeService.isAlive();
    }

    @Override
    @GetMapping("sayMyName")
    public String sayMyName(@RequestParam String name) {
        return "Your name is: " + name;
    }

    @Override
    @GetMapping("sayMyOtherName/{name}")
    public String sayMyOtherName(@PathVariable("name") String name) {
        return "Your name is: " + name;
    }
}
