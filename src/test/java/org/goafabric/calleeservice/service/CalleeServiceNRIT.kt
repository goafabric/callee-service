package org.goafabric.calleeservice.service

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.goafabric.calleeservice.adapter.CalleeServiceAdapter
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
internal class CalleeServiceNRIT(
    @Autowired private val calleeServiceAdapter: CalleeServiceAdapter) {

    @Test
    fun sayMyName() {
        assertThat(calleeServiceAdapter.sayMyName("Heisenberg")!!.message)
            .isEqualTo("Your name is: Heisenberg")
    }
}