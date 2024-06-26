package org.goafabric.calleeservice.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CalleeServiceAdapterIT {
    @Autowired
    CalleeServiceAdapter calleeServiceAdapterDec;

    @Test
    void sayMyName() {
        assertThat(calleeServiceAdapterDec.sayMyName("Heisenberg").message())
                .isEqualTo("Your name is: Heisenberg");
    }

    @Test
    void sayMyOtherName() {
        assertThat(calleeServiceAdapterDec.sayMyOtherName("SlimShady").message())
                .isEqualTo("Your other name is: SlimShady");
    }
}

