package org.goafabric.calleeservice.controller;

import org.goafabric.calleeservice.controller.dto.Callee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalleeControllerIT {
    @Autowired
    private CalleeController calleeController;

    @Test
    void sayMyName() {
        assertThat(calleeController.sayMyName("Heisenberg").message())
                .isNotNull().isEqualTo("Your name is: Heisenberg");
    }

    @Test
    void sayMyOtherName() {
        assertThat(calleeController.sayMyOtherName("SlimShady").message())
                .isNotNull().isEqualTo("Your other name is: SlimShady");
    }

    @Test
    void save() {
        assertThat(calleeController.save(new Callee("0", "Secret")).message())
                .isEqualTo("Storing your message: Secret");
    }

}
