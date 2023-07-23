package org.goafabric.calleeservice.logic;

import org.goafabric.calleeservice.controller.Callee;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CalleeLogicTest {

    @Test
    void sayMyName() {
        assertThat(new CalleeLogic().sayMyName("Heisenberg").message())
                .isEqualTo("Your name is: Heisenberg");
    }

    @Test
    void sayMyOtherName() {
        assertThat(new CalleeLogic().sayMyOtherName("Slim Shady").message())
                .isEqualTo("Your other name is: Slim Shady");
    }

    @Test
    void save() {
        assertThat(new CalleeLogic().save(new Callee("0", "saved by the bell")).message())
                .isEqualTo("Storing your message: saved by the bell");
    }
}