package org.goafabric.calleeservice.logic;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class MapReduceTest {

    @Test
    public void reduce() {
        int sum = Stream.of(1, 5, 8, -9, -5)
                .map(Math::abs).reduce(Integer::sum).get();
    }

    @Test
    public void reduceKotlin() {
        val sum = listOf(1, 5, 8, -9, -5)
                .map { a -> abs(a) }.reduce(Integer::sum)
    }


    //persons.filter { p -> p.lastName == "Simpson" }
    //persons.stream().filter(p -> p.getLastName().equals("Simpson")).toList();

}
