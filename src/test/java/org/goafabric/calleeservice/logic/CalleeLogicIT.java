package org.goafabric.calleeservice.logic;

import org.goafabric.calleeservice.controller.Callee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalleeLogicIT {
    @Autowired
    private CalleeLogic calleeLogic;

    @Test
    void sayMyName() {
        assertThat(calleeLogic.sayMyName("Heisenberg").message())
                .isNotNull().isEqualTo("Your name is: Heisenberg");
    }

    @Test
    void sayMyOtherName() {
        assertThat(calleeLogic.sayMyOtherName("SlimShady").message())
                .isNotNull().isEqualTo("Your other name is: SlimShady");
    }

    @Test
    void save() {
        assertThat(calleeLogic.save(new Callee("0", "Secret")).message())
                .isEqualTo("Storing your message: Secret");
    }

}
