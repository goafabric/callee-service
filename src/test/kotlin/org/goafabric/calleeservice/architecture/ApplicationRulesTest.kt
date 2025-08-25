package org.goafabric.calleeservice.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.core.importer.Location
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.goafabric.calleeservice.Application
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.scheduling.annotation.Async
import kotlin.jvm.java

@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class, ApplicationRulesTest.IgnoreCglib::class])
object ApplicationRulesTest {
    @ArchTest
    val reflectionShouldBeAvoided: ArchRule = ArchRuleDefinition.noClasses()
        .that()
        .areNotAnnotatedWith(Configuration::class.java)
        .and()
        .doNotImplement(RuntimeHintsRegistrar::class.java)
        .and()
        .haveSimpleNameNotContaining("AuditTrailListener")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("java.lang.reflect")
        .orShould()
        .callMethod(Class::class.java, "forName", String::class.java)
        .orShould()
        .dependOnClassesThat()
        .haveFullyQualifiedName("org.springframework.util.ReflectionUtils")
        .orShould()
        .dependOnClassesThat()
        .haveFullyQualifiedName("org.springframework.beans.BeanWrapperImpl")
        .orShould()
        .dependOnClassesThat()
        .haveFullyQualifiedName("jakarta.validation.ConstraintValidator")
        .orShould()
        .dependOnClassesThat()
        .areAnnotatedWith(ConditionalOnProperty::class.java)
        .because("Reflection breaks native image support");


    @ArchTest
    val aspectsNeedRuntimeHint: ArchRule = ArchRuleDefinition.classes()
        .that()
        .areAnnotatedWith("org.aspectj.lang.annotation.Aspect").should()
        .beAnnotatedWith(ImportRuntimeHints::class.java)
        .allowEmptyShould(true)
        .because("Aspects need a Reflection RuntimeHint with MemberCategory.INVOKE_DECLARED_METHODS")

    @ArchTest
    val librariesThatAreBanished: ArchRule = ArchRuleDefinition.noClasses()
        .should()
        .dependOnClassesThat()
        .resideInAPackage("com.google.common..")
        .orShould()
        .dependOnClassesThat()
        .resideInAPackage("org.apache.commons..")
        .because("Java 21+ and Spring cover the functionality already, managing extra libraries with transient dependencies should be avoided")

    @ArchTest
    val onlyAllowedLibraries: ArchRule = ArchRuleDefinition.classes()
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage(
            "org.goafabric..",
            "java..",
            "javax..",
            "jakarta..",
            "org.springframework..",
            "org.slf4j..",
            "com.fasterxml.jackson..",
            "org.flywaydb..",
            "org.hibernate..",
            "org.mapstruct..",
            "io.github.resilience4j..",
            "io.micrometer..",
            "org.springdoc..",
            "net.ttddyy..",

            "io.swagger.v3..",
            "com.github.benmanes.caffeine..",
            "com.azure.storage.blob..",
            "software.amazon.awssdk..", "io.awspring.cloud..",

            "org.javers..",
            "com.nimbusds.jwt..",

            "kotlin..",
            "kotlinx..",
            "org.jetbrains.annotations.."
        )
        .because("Only core and allowed libraries should be used to avoid unnecessary third-party dependencies")

    @ArchTest
    val componentNamesThatAreBanished: ArchRule = ArchRuleDefinition.noClasses()
        .that().haveSimpleNameNotContaining("Mapper")
        .should()
        .haveSimpleNameEndingWith("Impl")
        .andShould()
        .haveSimpleNameEndingWith("Management")
        .because("Avoid filler names like Impl or Management, use neutral Bean instead")

    @ArchTest
    val asyncIsBanished: ArchRule = ArchRuleDefinition.noMethods().should().beAnnotatedWith(Async::class.java)
        .because("Using Async leads to ThreadLocals being erased, Exceptions being swallowed, Resilience4j not working and possible Concurrency Issues in General")

    @ArchTest
    val flywayJavaMigrationsAreBanished: ArchRule = ArchRuleDefinition.noClasses().should().dependOnClassesThat()
        .resideInAnyPackage("org.flywaydb.core.api.migration..")
        .because("Flyway Java Migrations should not be used, complex import logic should go to a separate batch, simple ones with a simple Java class if aware of the consequences")

    internal class IgnoreCglib : ImportOption {
        override fun includes(location: Location): Boolean {
            return !location.contains("$$") && !location.contains("EnhancerByCGLIB")
        }
    }
}
