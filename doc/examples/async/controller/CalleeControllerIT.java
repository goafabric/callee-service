package org.goafabric.calleeservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalleeControllerIT {
    @Autowired
    CalleeController calleeController;

    @Test
    public void sayMyName() throws ExecutionException, InterruptedException {
        var future = calleeController.sayMyName("yo");
        var callee = future.get(); //this will essentially block
        //todo non blocking alternative
        System.out.println(callee);
    }

}