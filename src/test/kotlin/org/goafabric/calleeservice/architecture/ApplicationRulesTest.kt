package org.goafabric.calleeservice.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.core.importer.Location
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.goafabric.calleeservice.Application
import org.goafabric.calleeservice.architecture.ApplicationRulesTest.IgnoreCglib
import org.springframework.context.annotation.ImportRuntimeHints

@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class, IgnoreCglib::class])
object ApplicationRulesTest {
    @ArchTest
    val reflectionShouldBeAvoided: ArchRule = ArchRuleDefinition.noClasses()
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
        .because("Reflection breaks native image support")

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
    val componentNamesThatAreBanished: ArchRule = ArchRuleDefinition.noClasses()
        .that().haveSimpleNameNotContaining("Mapper")
        .should()
        .haveSimpleNameEndingWith("Impl")
        .andShould()
        .haveSimpleNameEndingWith("Management")
        .because("Avoid filler names like Impl or Management, use neutral Bean instead")

    internal class IgnoreCglib : ImportOption {
        override fun includes(location: Location): Boolean {
            return !location.contains("$$") && !location.contains("EnhancerByCGLIB")
        }
    }
}
