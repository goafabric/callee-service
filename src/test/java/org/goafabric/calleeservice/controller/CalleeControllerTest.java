package org.goafabric.calleeservice.controller;

import org.goafabric.calleeservice.logic.CalleeLogic;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class CalleeControllerTest {
    private CalleeLogic logic = Mockito.mock(CalleeLogic.class);
    private CalleeController controller = new CalleeController(logic);

    @Test
    void sayMyName() {
        assertThat(controller.sayMyName("")).isNull();
    }

    @Test
    void sayMyOtherName() {
        assertThat(controller.sayMyOtherName("")).isNull();
    }

    @Test
    void save() {
        assertThat(controller.save(new Callee("", ""))).isNull();
    }
}