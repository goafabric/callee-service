package org.goafabric.calleeservice.logic;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.goafabric.calleeservice.service.Callee;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class CalleeLogic {
    private final AtomicLong sleepTime = new AtomicLong(0L);

    public Callee sayMyName(@NonNull String name) {
        sleep();
        return Callee.builder()
                .message("Your name is: " + name).build();
    }

    public Callee sayMyOtherName(@NonNull String name) {
        sleep();
        return Callee.builder()
                .message("Your name is: " + name).build();
    }

    public Callee setSleepTime(@NonNull Long sleepTime) {
        this.sleepTime.set(sleepTime); //you should never change instance variables for a productive app
        return Callee.builder()
                .message("set sleepTime to: " + sleepTime).build();
    }

    @SneakyThrows
    private void sleep() {
        Thread.sleep(sleepTime.get());
    }
}