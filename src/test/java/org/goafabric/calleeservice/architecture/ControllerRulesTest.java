package org.goafabric.calleeservice.architecture;


import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.goafabric.calleeservice.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packagesOf = Application.class, importOptions = DoNotIncludeTests.class)
class ControllerRulesTest {

    @ArchTest
    static final ArchRule layerAreRespectedBasic = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()

            .layer("Controller").definedBy("..controller")
            .layer("Logic").definedBy("..logic..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Logic").mayOnlyBeAccessedByLayers("Controller");

    @ArchTest
    static final ArchRule controllerNaming = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith("Controller");

    @ArchTest
    static final ArchRule recordsShouldBeUsedForDtos = classes()
            .that().resideInAPackage("..dto")
            .should().beRecords()
            .because("Classes in the dto package should be records");

    @ArchTest
    static final ArchRule controllerShouldNotUseCrossOrigin = classes()
            .should().notBeAnnotatedWith(CrossOrigin.class);

    @ArchTest
    static final ArchRule controllerShouldNotReturnResponseEntity = methods()
            .that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should(new ArchCondition<>("ResponseEntity check") {
                @Override
                public void check(JavaMethod method, ConditionEvents events) {
                    if (method.getRawReturnType().isAssignableFrom(ResponseEntity.class)) {
                        events.add(SimpleConditionEvent.violated(method,
                                String.format("Method %s in %s returns ResponseEntity", method.getName(), method.getOwner().getName())));
                    }
                }
            });
}
