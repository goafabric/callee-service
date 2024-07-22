package org.goafabric.calleeservice.logic

import org.assertj.core.api.Assertions.assertThat
import org.goafabric.calleeservice.controller.dto.Callee
import org.junit.jupiter.api.Test

internal class CalleeLogicTest {
    private val calleeLogic = CalleeLogic()

    @Test
    fun sayMyName() {
        assertThat(calleeLogic.sayMyName("Heisenberg").message)
            .isEqualTo("Your name is: Heisenberg")
    }

    @Test
    fun sayMyOtherName() {
        assertThat(calleeLogic.sayMyOtherName("Slim Shady").message)
            .isEqualTo("Your other name is: Slim Shady")
    }

    @Test
    fun save() {
        assertThat(calleeLogic.save(Callee("0", "saved by the bell"))!!.message)
            .isEqualTo("Storing your message: Callee(id=0, message=saved by the bell).message")
    }
}