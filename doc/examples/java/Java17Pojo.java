package org.goafabric.spring.boot.examplebatch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

public class Java17Pojo {

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

/*
    # One
    should use Constructor injection, without Lombok, without @Autowired
    should use Lombok for POJOs only, and omit usage everywhere else
    could use Records for POJOs,  except for JPA entitities
    could use java Var

    # Why
    Constructor injection has become the standard, while @Autowired is not needed, other annotations (e.g. @Value) might
    Lombok still seems like a good choice for entities, however good support for future Java versions seems to decline
    Records may help to get rid of Lombok, but lack named and optional args, which could yield to error prone code
    Var may increase readability in certain scenarios and decrease in others, no support for instances or final val

    # Better solution
    Use Kotlin, Injection, POJO and Val support solves all of Java's flaws and it replaces Lombok entirely (@Data, @Delegate, @SneakyThrows...)
*/