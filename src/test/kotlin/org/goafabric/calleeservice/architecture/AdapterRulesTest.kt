package org.goafabric.calleeservice.architecture

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.goafabric.calleeservice.Application
import org.springframework.context.annotation.Configuration
import org.springframework.web.service.annotation.HttpExchange

@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class])
internal object AdapterRulesTest {
    @ArchTest
    val adapterName: ArchRule = ArchRuleDefinition.methods().that()
        .areMetaAnnotatedWith(HttpExchange::class.java)
        .should().beDeclaredInClassesThat().haveSimpleNameEndingWith("Adapter")
        .allowEmptyShould(true)

    @ArchTest
    val declarativeClientShouldBeUsed: ArchRule = ArchRuleDefinition.noClasses().that()
        .areNotAnnotatedWith(Configuration::class.java)
        .should()
        .dependOnClassesThat()
        .haveFullyQualifiedName("org.springframework.web.client.RestClient")
        .orShould().dependOnClassesThat()
        .haveFullyQualifiedName("org.springframework.web.client.RestTemplate")
        .orShould().dependOnClassesThat()
        .haveFullyQualifiedName("org.springframework.web.reactive.function.client.WebClient")
        .`as`("Only use the declarative REST Client, as otherwise native image support is broken")
        .allowEmptyShould(false)


    @ArchTest
    val declarativeClientShouldUserCircuitBreaker: ArchRule = ArchRuleDefinition.methods().that()
        .areMetaAnnotatedWith(HttpExchange::class.java)
        .should()
        .beDeclaredInClassesThat()
        .areMetaAnnotatedWith("io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker")
        .allowEmptyShould(true)
}
