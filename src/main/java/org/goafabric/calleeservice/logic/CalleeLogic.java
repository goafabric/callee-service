package org.goafabric.calleeservice.logic;

import org.goafabric.calleeservice.controller.Callee;
import org.goafabric.calleeservice.crossfunctional.DurationLog;
import org.springframework.stereotype.Component;

@Component
@DurationLog
public class CalleeLogic {
    public Callee sayMyName(String name) {
        return Callee.builder()
                .message("Your name is: " + name).build();
    }

    public Callee sayMyOtherName(String name) {
        return Callee.builder()
                .message("Your name is: " + name).build();
    }

    public Callee save(Callee callee) {
        return Callee.builder()
                .message("Storing your message: " + callee.getMessage()).build();
    }
}