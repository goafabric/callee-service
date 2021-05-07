package org.goafabric.calleservice.logic;

import org.springframework.stereotype.Component;

@Component
public class CalleLogic {
    public Boolean isAlive() {
        return Boolean.TRUE;
    }

    public String sayMyName(String name) {
        return "Your name is: " + name;
    }

    public String sayMyOtherName(String name) {
        return "Your name is: " + name;
    }

}
