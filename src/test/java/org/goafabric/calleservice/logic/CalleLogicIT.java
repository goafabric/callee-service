package org.goafabric.calleservice.logic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalleLogicIT {
    @Autowired
    private CalleLogic calleLogic;

    @Test
    void isAlive() {
        assertThat(calleLogic.isAlive()).isEqualTo(Boolean.TRUE);
    }

    @Test
    void sayMyName() {
        assertThat(calleLogic.sayMyName("SlimShady")).isEqualTo("Your name is: SlimShady");
    }

    @Test
    void sayMyOtherName() {
        assertThat(calleLogic.sayMyOtherName("SlimShady")).isEqualTo("Your name is: SlimShady");
    }
}
