package org.goafabric.calleeservice.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CalleeServiceAdapterIT {
    @Autowired
    CalleeServiceAdapter calleeServiceAdapter;

    @Test
    public void sayMyName() {
        assertThat(calleeServiceAdapter.sayMyName("Heisenberg").getMessage()).isEqualTo("Your name is: Heisenberg");
    }

    public void sayMyOtherName() {
        assertThat(calleeServiceAdapter.sayMyOtherName("SlimShady").getMessage()).isEqualTo("Your name is: SlimShady");
    }
}
