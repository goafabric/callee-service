package org.goafabric.calleeservice.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CalleeControllerIT (
    @Autowired
    private val calleeController: CalleeController) {

    @Test
    fun sayMyName() {
        assertThat(calleeController.sayMyName("Heisenberg").message)
            .isNotNull().isEqualTo("Your name is: Heisenberg")
    }

    @Test
    fun sayMyOtherName() {
        assertThat(calleeController.sayMyOtherName("SlimShady").message)
            .isNotNull().isEqualTo("Your other name is: SlimShady")
    }

    @Test
    @Disabled //todo
    fun save() {
        assertThat(calleeController.save(Callee("0", "Secret"))!!.message)
            .isEqualTo("Storing your message: Secret")
    }
}
