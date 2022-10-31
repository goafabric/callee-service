package org.goafabric.calleeservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ApplicationIT {

    @Test
    public void test() {
        assertThat(Application.readFile()).isNotNull();
    }
}