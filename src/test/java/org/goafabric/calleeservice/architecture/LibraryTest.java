package org.goafabric.calleeservice.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "org.goafabric", importOptions = ImportOption.DoNotIncludeTests.class)
public class LibraryTest {
    @ArchTest
    static final ArchRule libraries =
            noClasses()
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("com.google.common..")
                    .orShould()
                    .dependOnClassesThat()
                    .resideInAPackage("org.apache.commons..");
}
