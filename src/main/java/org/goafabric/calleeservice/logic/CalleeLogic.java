package org.goafabric.calleeservice.logic;

import lombok.NonNull;
import org.goafabric.calleeservice.service.Callee;
import org.springframework.stereotype.Component;

@Component
public class CalleeLogic {
    public Callee sayMyName(@NonNull String name) {
        return Callee.builder()
                .message("Your name is: " + name).build();
    }

    public Callee sayMyOtherName(@NonNull String name) {
        return Callee.builder()
                .message("Your name is: " + name).build();
    }

    public Callee save(@NonNull Callee callee) {
        return Callee.builder()
                .message("Storing your message: " + callee.getMessage()).build();
    }
}