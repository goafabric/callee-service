package org.goafabric.calleeservice.architecture;


import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameStartingWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "org.goafabric", importOptions = DoNotIncludeTests.class)
class LayerCodingRulesTest {

    @ArchTest
    static final ArchRule layers_are_respected = layeredArchitecture()
        .consideringOnlyDependenciesInLayers()
        .ignoreDependency(simpleNameStartingWith("DemoDataImporter"), DescribedPredicate.alwaysTrue())

        .layer("Controller").definedBy("..controller")
        .layer("Logic").definedBy("..logic..")
        //.layer("Persistence").definedBy("..persistence..")

        .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
        .whereLayer("Logic").mayOnlyBeAccessedByLayers("Controller");
        //.whereLayer("Persistence").mayOnlyBeAccessedByLayers("Logic")

    @ArchTest
    static final ArchRule controller_naming = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith("Controller");

    @ArchTest
    static final ArchRule records = classes()
            .that().resideInAPackage("..dto")
            .should().beRecords()
            .because("Classes in the dto package should be records");

    @ArchTest
    static final ArchRule controller_return = methods()
            .that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should(new ArchCondition<>("ResponseEntity check") {
                @Override
                public void check(JavaMethod method, ConditionEvents events) {
                    if (method.getRawReturnType().isAssignableFrom(ResponseEntity.class)) {
                        String message = String.format("Method %s in %s returns ResponseEntity",
                                method.getName(), method.getOwner().getName());
                        events.add(SimpleConditionEvent.violated(method, message));
                    }
                }
            });
}
