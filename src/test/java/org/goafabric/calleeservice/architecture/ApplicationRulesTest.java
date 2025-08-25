package org.goafabric.calleeservice.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.goafabric.calleeservice.Application;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.scheduling.annotation.Async;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(packagesOf = Application.class, importOptions = {ImportOption.DoNotIncludeTests.class, ApplicationRulesTest.IgnoreCglib.class})
public class ApplicationRulesTest {

    static class IgnoreCglib implements ImportOption {
        @Override
        public boolean includes(Location location) {
            return !location.contains("$$") && !location.contains("EnhancerByCGLIB");
        }
    }

    @ArchTest
    static final ArchRule reflectionShouldBeAvoided =
            noClasses()
                    .that()
                    .areNotAnnotatedWith(Configuration.class)
                    .and()
                    .doNotImplement(RuntimeHintsRegistrar.class)
                    .and()
                    .haveSimpleNameNotContaining("AuditTrailListener")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("java.lang.reflect")
                    .orShould()
                    .callMethod(Class.class, "forName", String.class)
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
                    .areAnnotatedWith(ConditionalOnProperty.class)
                    .because("Reflection breaks native image support");

    @ArchTest
    static final ArchRule aspectsNeedRuntimeHint =
            classes()
                    .that()
                    .areAnnotatedWith("org.aspectj.lang.annotation.Aspect").should()
                    .beAnnotatedWith(ImportRuntimeHints.class)
                    .allowEmptyShould(true)
                    .because("Aspects need a Reflection RuntimeHint with MemberCategory.INVOKE_DECLARED_METHODS");

    @ArchTest
    static final ArchRule librariesThatAreBanished =
            noClasses()
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("com.google.common..")
                    .orShould()
                    .dependOnClassesThat()
                    .resideInAPackage("dev.mccue.guava..")
                    .orShould()
                    .dependOnClassesThat()
                    .resideInAPackage("org.apache.commons..")
                    .because("Java 21+ and Spring cover the functionality already, managing extra libraries with transient dependencies should be avoided");


    @ArchTest
    static final ArchRule onlyAllowedLibraries = ArchRuleDefinition.classes()
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                    "org.goafabric..",
                    "java..",
                    "javax..",
                    "jakarta..",
                    "org.springframework..",
                    "org.slf4j..",
                    "org.aspectj.lang..",
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
                    "software.amazon.awssdk",

                    "org.javers..",
                    "com.nimbusds.jwt..",

                    "kotlin..",
                    "kotlinx..",
                    "org.jetbrains.annotations.."
            )
            .because("Only core and allowed libraries should be used to avoid unnecessary third-party dependencies");


    @ArchTest
    static final ArchRule componentNamesThatAreBanished = noClasses()
            .that().haveSimpleNameNotContaining("Mapper")
            .should()
            .haveSimpleNameEndingWith("Impl")
            .andShould()
            .haveSimpleNameEndingWith("Management")
            .because("Avoid filler names like Impl or Management, use neutral Bean instead");

    @ArchTest
    static final ArchRule asyncIsBanished =
            noMethods().should().beAnnotatedWith(Async.class)
                    .because("Using Async leads to ThreadLocals being erased, Exceptions being swallowed, Resilience4j not working and possible Concurrency Issues in General");

    @ArchTest
    static final ArchRule flywayJavaMigrationsAreBanished =
            noClasses().should().dependOnClassesThat()
                    .resideInAnyPackage("org.flywaydb.core.api.migration..")
                    .because("Flyway Java Migrations should not be used, complex import logic should go to a separate batch, simple ones with a simple Java class if aware of the consequences");

}
