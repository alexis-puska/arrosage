package fr.iocean.arrosage;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("fr.iocean.arrosage");

        noClasses()
            .that()
                .resideInAnyPackage("fr.iocean.arrosage.service..")
            .or()
                .resideInAnyPackage("fr.iocean.arrosage.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..fr.iocean.arrosage.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
