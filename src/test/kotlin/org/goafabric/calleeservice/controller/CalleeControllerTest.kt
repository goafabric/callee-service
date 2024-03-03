package org.goafabric.calleeservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.calleeservice.logic.CalleeLogic
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq


internal class CalleeControllerTest {
    private val calleeLogic: CalleeLogic = Mockito.mock(CalleeLogic::class.java)
    private val calleController = CalleeController(calleeLogic)

    @Test
    fun sayMyName() {
        Mockito.`when`(calleeLogic.sayMyName(eq("Heisenberg"))).thenReturn(Callee("", "Heisenberg"))
        assertThat(calleController.sayMyName("Heisenberg").message)
            .isEqualTo("Heisenberg")
    }

    @Test
    fun sayMyOtherName() {
        Mockito.`when`(calleeLogic.sayMyOtherName(eq("Slim Shady")))
            .thenReturn(Callee("", "Slim Shady"))
        assertThat(calleController.sayMyOtherName("Slim Shady").message)
            .isEqualTo("Slim Shady")
    }

    @Test
    fun save() {
        Mockito.`when`(calleeLogic.save(any())).thenReturn(Callee("", "saved"))
        assertThat(calleController.save(Callee("", "saved"))!!.message)
            .isEqualTo("saved")
        Mockito.verify(calleeLogic, Mockito.times(1)).save(Callee("", "saved"))
    }
}
