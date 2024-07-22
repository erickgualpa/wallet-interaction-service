package org.egualpam.contexts.payment.walletinteractionservice.architecture

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class HexagonalArchitectureTest {

  private val importedClasses =
      ClassFileImporter().importPackages("org.egualpam.contexts.payment.walletinteractionservice")

  @Test
  fun `domain should not depend on application ports`() {
    noClasses().that().resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..ports..")
        .check(importedClasses)
  }

  @Test
  fun `domain should not depend on application use cases`() {
    noClasses().that().resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..usecases..")
        .check(importedClasses)
  }

  @Test
  fun `domain should not depend on adapters`() {
    noClasses().that().resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..adapters..")
        .check(importedClasses)
  }

  @Test
  fun `application should not depend on adapters`() {
    noClasses().that().resideInAPackage("..application..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..adapters..")
        .check(importedClasses)
  }
}
