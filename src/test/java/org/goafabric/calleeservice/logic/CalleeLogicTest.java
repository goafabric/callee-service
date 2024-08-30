package org.goafabric.calleeservice.logic;

import org.goafabric.calleeservice.controller.dto.Callee;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CalleeLogicTest {
    private CalleeLogic calleeLogic = new CalleeLogic();

    @Test
    void sayMyName() {
        assertThat(calleeLogic.sayMyName("Heisenberg").message())
                .isEqualTo("Your name is: Heisenberg");
    }

    @Test
    void sayMyOtherName() {
        assertThat(calleeLogic.sayMyOtherName("Slim Shady").message())
                .isEqualTo("Your other name is: Slim Shady");
    }

    @Test
    void save() {
        assertThat(calleeLogic.save(new Callee("0", "saved by the bell")).message())
                .isEqualTo("Storing your message: saved by the bell");
    }
}