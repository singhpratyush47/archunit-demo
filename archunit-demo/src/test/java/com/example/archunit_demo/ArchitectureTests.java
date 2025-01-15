package com.example.archunit_demo;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.CompositeArchRule;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.conditions.ArchConditions.dependOnClassesThat;
import static com.tngtech.archunit.lang.conditions.ArchConditions.not;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.are;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static de.rweisleder.archunit.spring.SpringAnnotationPredicates.springAnnotatedWith;
import static de.rweisleder.archunit.spring.framework.SpringComponentPredicates.springController;

@AnalyzeClasses(packagesOf = ArchunitDemoApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTests {

    @ArchTest
    ArchRule controllerNaming = classes().that().areAnnotatedWith(RestController.class)
            .or().haveSimpleNameEndingWith("Controller")
            .should().beAnnotatedWith(RestController.class)
            .andShould().haveSimpleNameEndingWith("Controller")
            .because("Controller should be easy to find");

    @ArchTest
    ArchRule requestMappingMethod = methods().that(are(springAnnotatedWith(RequestMapping.class)))
            .or(are(springAnnotatedWith(GetMapping.class)))
            .should().beDeclaredInClassesThat(are(springController()));


    @ArchTest
    ArchRule dependenciesBetweenModule = CompositeArchRule
            .of(
                    classes()
                            .that().resideInAPackage("..controller..")
                            .should(not(dependOnClassesThat(resideInAPackage("..repository..")))))
            .and(
                    classes()
                            .that().resideInAPackage("..repository..")
                            .should(not(dependOnClassesThat(resideInAPackage("..controller..")))));
}
