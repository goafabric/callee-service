package org.goafabric.calleeservice.logic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalleeLogicIT {
    @Autowired
    private CalleeLogic calleeLogic;

    @Test
    void isAlive() {
        assertThat(calleeLogic.isAlive()).isEqualTo(Boolean.TRUE);
    }

    @Test
    void sayMyName() {
        assertThat(calleeLogic.sayMyName("SlimShady")).isEqualTo("Your name is: SlimShady");
    }

    @Test
    void sayMyOtherName() {
        assertThat(calleeLogic.sayMyOtherName("SlimShady")).isEqualTo("Your name is: SlimShady");
    }
}
