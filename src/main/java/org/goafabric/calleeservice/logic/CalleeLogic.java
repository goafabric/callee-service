package org.goafabric.calleeservice.logic;

import lombok.SneakyThrows;
import org.goafabric.calleeservice.service.Callee;
import org.springframework.stereotype.Component;

@Component
public class CalleeLogic {
    private Long sleepTime = 0l;

    public Callee sayMyName(String name) {
        sleep();
        return Callee.builder()
                .message("Your name is: " + name).build();
    }

    public Callee sayMyOtherName(String name) {
        sleep();
        return Callee.builder()
                .message("Your name is: " + name).build();
    }

    public Callee setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime; //you should never change instance variables for a productive app
        return Callee.builder()
                .message("set sleepTime to: " + sleepTime).build();
    }

    @SneakyThrows
    private void sleep() {
        Thread.sleep(sleepTime);
    }
}