package org.goafabric.calleeservice.remote;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalleControllerRemoteIT {
    @Autowired
    private CalleeControllerRemote calleeControllerRemote;

    @Test
    void sayMyName() {
        Assertions.assertThat(calleeControllerRemote.sayMyName("Heisenberg").message())
                .isEqualTo("Your name is: Heisenberg");
    }

    @Test
    void sayMyOtherName() {
        Assertions.assertThat(calleeControllerRemote.sayMyOtherName("SlimShady").message())
                .isEqualTo("Your other name is: SlimShady");
    }
}

