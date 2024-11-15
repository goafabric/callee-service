package org.goafabric.calleeservice.adapter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalleeServiceAdapterIT {
    @Autowired
    private CalleeServiceAdapter calleeServiceAdapterDec;

    @Test
    void sayMyName() {
        Assertions.assertThat(calleeServiceAdapterDec.sayMyName("Heisenberg").message())
                .isEqualTo("Your name is: Heisenberg");
    }

    @Test
    void sayMyOtherName() {
        Assertions.assertThat(calleeServiceAdapterDec.sayMyOtherName("SlimShady").message())
                .isEqualTo("Your other name is: SlimShady");
    }
}

