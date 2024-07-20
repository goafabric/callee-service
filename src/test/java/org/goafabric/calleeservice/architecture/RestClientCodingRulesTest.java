package org.goafabric.calleeservice.architecture;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.annotation.HttpExchange;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "org.goafabric", importOptions = DoNotIncludeTests.class)
class RestClientCodingRulesTest {

    @ArchTest
    static final ArchRule declarative_client_should_only_be_used =
        noClasses().that()
            .areNotAnnotatedWith(Configuration.class)
            .should()
            .dependOnClassesThat()
            .haveFullyQualifiedName("org.springframework.web.client.RestClient")
            .orShould().dependOnClassesThat()
            .haveFullyQualifiedName("org.springframework.web.client.RestTemplate")
            .orShould().dependOnClassesThat()
            .haveFullyQualifiedName("org.springframework.web.reactive.function.client.WebClient")
            .as("Only use the declarative REST Client, as otherwise native image support is broken")
            .allowEmptyShould(true);


    @ArchTest
    static final ArchRule declarative_client_should_use_circuit_breaker =
        methods().that()
            .areMetaAnnotatedWith(HttpExchange.class)
            .should()
            .beDeclaredInClassesThat()
            .areMetaAnnotatedWith("io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker")
            .allowEmptyShould(true);
}
