package org.goafabric.calleeservice.logic;

import org.goafabric.calleeservice.controller.Callee;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class CalleeLogic {
    private final AtomicLong sleepTime = new AtomicLong(0L);

    public Callee sayMyName(String name) {
        sleep();
        return new Callee("0", "Your name is: " + name);
    }

    public Callee sayMyOtherName(String name) {
        sleep();
        return new Callee("0", "Your other name is: " + name);
    }

    public Callee save(Callee callee) {
        return new Callee("0", "Storing your message: " + callee.message());
    }

    public Callee setSleepTime(Long sleepTime) {
        this.sleepTime.set(sleepTime); //you should never change instance variables for a productive app
        return new Callee("0", "set sleepTime to:" + sleepTime);
    }

    private void sleep() {
        try {
            Thread.sleep(sleepTime.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}