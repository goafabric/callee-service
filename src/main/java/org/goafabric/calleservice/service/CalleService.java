package org.goafabric.calleservice.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = CalleService.RESOURCE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public interface CalleService {
    String RESOURCE = "callees";

    @GetMapping("isAlive")
    Boolean isAlive();

    @GetMapping("setSleepTime")
    String setSleepTime(@RequestParam Long sleepTime);

    @GetMapping("sayMyName")
    String sayMyName(@RequestParam String name);

    @GetMapping("sayMyOtherName/{name}")
    String sayMyOtherName(@PathVariable String name);
}
