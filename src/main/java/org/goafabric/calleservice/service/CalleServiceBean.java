package org.goafabric.calleservice.service;

import org.goafabric.calleservice.logic.CalleeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
