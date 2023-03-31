package org.goafabric.calleeservice.declarative;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CalleeServiceAdapterDecIT {
    @Autowired
    CalleeServiceAdapterDec calleeServiceAdapterDec;

    @Test
    public void sayMyName() {
        assertThat(calleeServiceAdapterDec.sayMyName("Heisenberg").message())
                .isEqualTo("Your name is: Heisenberg");
    }

    @Test
    public void sayMyOtherName() {
        assertThat(calleeServiceAdapterDec.sayMyOtherName("SlimShady").message())
                .isEqualTo("Your other name is: SlimShady");
    }
}

