package org.goafabric.calleservice.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = CalleService.RESOURCE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public interface CalleService {
    String RESOURCE = "callees";

    @GetMapping("isAlive")
    Boolean isAlive();

    @GetMapping("sayMyName")
    String sayMyName(String name);

    @GetMapping("sayMyOtherName/{name}")
    String sayMyOtherName(String name);
}
