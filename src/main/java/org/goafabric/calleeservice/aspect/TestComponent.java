package org.goafabric.calleeservice.aspect;

import org.springframework.stereotype.Component;

@Component
@TestAnnotation
public class TestComponent {
    public void callOnMe() {
        System.err.println("inside method ...");
    }
}
