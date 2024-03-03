package org.goafabric.calleeservice.logic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CalleeLogicIT(
    @Autowired private val calleeLogic : CalleeLogic) {

    @Test
    fun sayMyName() {
        assertThat(calleeLogic.sayMyName("Heisenberg").message)
            .isNotNull.isEqualTo("Your name is: Heisenberg");
    }

    @Test
    fun sayMyOtherName() {
        assertThat(calleeLogic.sayMyOtherName("SlimShady").message)
            .isNotNull.isEqualTo("Your other name is: SlimShady");
    }

}