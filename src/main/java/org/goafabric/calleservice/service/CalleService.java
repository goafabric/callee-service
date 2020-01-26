package org.goafabric.calleservice.service;

public interface CalleService {
    String RESOURCE = "callees";
    Boolean isAlive();
    String sayMyName(String name);
    String sayMyOtherName(String name);
}
