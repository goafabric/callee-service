package org.goafabric.calleeservice.remote

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CalleControllerRemoteIT {
    @Autowired
    private val calleeControllerRemote: CalleeControllerRemote? = null

    @Test
    fun sayMyName() {
        Assertions.assertThat(calleeControllerRemote!!.sayMyName("Heisenberg").message)
            .isEqualTo("Your name is: Heisenberg")
    }

    @Test
    fun sayMyOtherName() {
        Assertions.assertThat(calleeControllerRemote!!.sayMyOtherName("SlimShady").message)
            .isEqualTo("Your other name is: SlimShady")
    }
}

