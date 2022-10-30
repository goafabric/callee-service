package org.goafabric.calleeservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationIT {

    @Test
    public void test() {
        SpringApplication.run(Application.class, "");
    }
}