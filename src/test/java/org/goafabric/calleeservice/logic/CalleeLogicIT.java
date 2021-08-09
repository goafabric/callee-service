package org.goafabric.calleeservice.logic;

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
        assertThat(calleeLogic.sayMyName("Heisenberg").getMessage())
                .isNotNull().isEqualTo("Your name is: Heisenberg");
    }

    @Test
    void sayMyOtherName() {
        assertThat(calleeLogic.sayMyOtherName("SlimShady").getMessage())
                .isNotNull().isEqualTo("Your name is: SlimShady");
    }

    @Test
    void setSleepTime() {
        assertThat(calleeLogic.setSleepTime(0l)).isNotNull();
    }
}
