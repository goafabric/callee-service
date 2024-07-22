package org.goafabric.calleeservice.architecture

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures
import org.goafabric.calleeservice.Application

@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class])
object PersistenceRulesTest {
    @ArchTest
    val layerAreRespectedWithPersistence: ArchRule = Architectures.layeredArchitecture()
        .consideringOnlyDependenciesInLayers()
        .ignoreDependency(
            JavaClass.Predicates.simpleNameStartingWith("DemoDataImporter"),
            DescribedPredicate.alwaysTrue()
        )

        .layer("Controller").definedBy("..controller")
        .layer("Logic").definedBy("..logic..")
        .layer("Persistence").definedBy("..persistence..")

        .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
        .whereLayer("Logic").mayOnlyBeAccessedByLayers("Controller")
        .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Logic")
        .allowEmptyShould(true)

    @ArchTest
    val classesExtendingRepositoryShouldEndWithRepository: ArchRule = ArchRuleDefinition.classes()
        .that().areAssignableTo("org.springframework.data.repository.Repository")
        .should().haveSimpleNameEndingWith("Repository")
        .because("all classes extending Repository should end with 'Repository' in their name")
        .allowEmptyShould(true)


    @ArchTest
    val logicAnnotatedWithTransactional: ArchRule = ArchRuleDefinition.classes()
        .that().areAssignableTo("org.springframework.data.repository.Repository")
        .should(object : ArchCondition<JavaClass?>("Repository used") {
            override fun check(item: JavaClass?, events: ConditionEvents) {
                ArchRuleDefinition.classes()
                    .that().haveSimpleNameEndingWith("Logic")
                    .should().beAnnotatedWith("org.springframework.transaction.annotation.Transactional")
                    .because("Logic Classes should be annotated with @Transactional")
                    .check(ClassFileImporter().importPackagesOf(Application::class.java))
            }
        }
        ).allowEmptyShould(true)
}
