package org.goafabric.spring.boot.examplebatch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

public class PojoTest {

    // Java 11 Lombok
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class PersonL {
        private String id;
        private String lastName;
        private String firstName;
    }

    @Test
    void lombok() {
        final PersonL person = PersonL.builder()
                .firstName("firstName")
                .lastName("lastName")
                .build();
        System.out.println(person.getFirstName());
    }

    // Java 17 Records
    record PersonR (
            String id,
            String firstName,
            String lastName
    ) {}
    
    @Test
    void java17() {
        final var person = new PersonR(
                null,
                "firstname",
                "lastname"
        );
        System.out.println(person.firstName());
    }

    // Kotlin
    data class PersonK (
            val id: String? = null,
            val firstName: String,
            val lastName: String
    )

    @Test
    fun kotlin() {
        val person = PersonK(
                firstName = "firstName",
                lastName = "lastName"
        )
        System.out.println(person.firstName);
    }
}
