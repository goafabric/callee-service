package org.goafabric.calleeservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "authorizations", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorizationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);
    @GetMapping("getTenantBySubject")
    public String getTenantBySubject (@RequestHeader HttpHeaders headers) {
        logger.info("headers found: ");
        headers.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains("x-"))
                .forEach(entry -> logger.info("{}: {}", entry.getKey(), entry.getValue()));
        return "42";
    }

}
