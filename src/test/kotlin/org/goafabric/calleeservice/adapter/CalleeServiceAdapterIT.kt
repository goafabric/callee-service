package org.goafabric.calleeservice.adapter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
internal class CalleeServiceAdapterIT(
    @Autowired private val calleeServiceAdapter: CalleeServiceAdapter) {

    @Test
    fun sayMyName() {
        assertThat(calleeServiceAdapter.sayMyName("Heisenberg")!!.message)
            .isEqualTo("Your name is: Heisenberg")
    }

    @Test
    fun sayMyOtherName() {
        assertThat(calleeServiceAdapter.sayMyName("SlimShady")!!.message)
            .isEqualTo("Your name is: SlimShady")
    }
}