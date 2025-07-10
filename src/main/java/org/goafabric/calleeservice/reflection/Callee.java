package org.goafabric.calleeservice.reflection;

import org.springframework.lang.NonNull;

public class Callee {
    @NonNull
    private String message = "Hello from Callee";

    @NonNull
    private String anotherProp = "yo";


    public String getMessage() {
        return message;
    }
}
