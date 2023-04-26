package org.goafabric.calleeservice.logic;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class MapReduceTest {

    @Test
    public void reduce() {
        int sum = Stream.of(1, 5, 8, -9, -5)
                .map(Math::abs).reduce(Integer::sum).get();
        System.err.println(sum);
    }

}
