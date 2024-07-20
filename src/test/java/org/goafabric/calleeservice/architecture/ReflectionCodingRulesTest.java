package org.goafabric.calleeservice.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.context.annotation.ImportRuntimeHints;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "org.goafabric", importOptions = {ImportOption.DoNotIncludeTests.class, ReflectionCodingRulesTest.IgnoreCglib.class})
public class ReflectionCodingRulesTest {
    static class IgnoreCglib implements ImportOption {
        @Override
        public boolean includes(Location location) {
            return !location.contains("$$") && !location.contains("EnhancerByCGLIB");
        }
    }
    
    @ArchTest
    static final ArchRule reflection =
            noClasses()
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
                    .because("Reflection breaks native image support");

    @ArchTest
    static final ArchRule aspect =
            classes()
                    .that()
                    .areAnnotatedWith("org.aspectj.lang.annotation.Aspect").should()
                    .beAnnotatedWith(ImportRuntimeHints.class)
                    .allowEmptyShould(true)
                    .because("Aspects need a Reflection RuntimeHint with MemberCategory.INVOKE_DECLARED_METHODS");
}
