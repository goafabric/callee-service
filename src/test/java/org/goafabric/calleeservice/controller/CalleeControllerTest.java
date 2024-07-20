package org.goafabric.calleeservice.controller;

import org.goafabric.calleeservice.controller.dto.Callee;
import org.goafabric.calleeservice.logic.CalleeLogic;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CalleeControllerTest {
    private CalleeLogic calleeLogic = Mockito.mock(CalleeLogic.class);
    private CalleeController calleController = new CalleeController(calleeLogic);

    @Test
    void sayMyName() {
        when(calleeLogic.sayMyName("Heisenberg")).thenReturn(new Callee("", "Heisenberg"));
        assertThat(calleController.sayMyName("Heisenberg").message())
                .isEqualTo("Heisenberg");
    }

    @Test
    void sayMyOtherName() {
        when(calleeLogic.sayMyOtherName("Slim Shady")).thenReturn(new Callee("", "Slim Shady"));
        assertThat(calleController.sayMyOtherName("Slim Shady").message())
                .isEqualTo("Slim Shady");
    }

    @Test

    void save() {
        when(calleeLogic.save(any(Callee.class))).thenReturn(new Callee("", "saved"));
        assertThat(calleController.save(new Callee("", "saved")).message())
                .isEqualTo("saved");
        verify(calleeLogic, times(1)).save(new Callee("", "saved"));
    }
}