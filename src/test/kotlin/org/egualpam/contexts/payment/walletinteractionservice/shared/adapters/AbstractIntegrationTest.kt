package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters

import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.AccountTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.DepositTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.OwnerTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.WalletTestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.ComposeContainer
import java.io.File

@ContextConfiguration(initializers = [AbstractIntegrationTest.Companion.MySQLInitializer::class])
@ActiveProfiles("integration-test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [WalletInteractionServiceApplication::class],
)
abstract class AbstractIntegrationTest {

  @Autowired
  protected lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var walletTestRepository: WalletTestRepository

  @Autowired
  protected lateinit var ownerTestRepository: OwnerTestRepository

  @Autowired
  protected lateinit var accountTestRepository: AccountTestRepository

  @Autowired
  protected lateinit var depositTestRepository: DepositTestRepository

  companion object {
    private const val MYSQL = "mysql"
    private const val MYSQL_PORT = 3306

    private val containers = ComposeContainer(File("docker-compose.yml"))
        .withExposedService(MYSQL, MYSQL_PORT)

    init {
      containers.start()
    }

    class MySQLInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
      override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val host = containers.getServiceHost(MYSQL, MYSQL_PORT)
        val port = containers.getServicePort(MYSQL, MYSQL_PORT)
        TestPropertyValues.of(
            "spring.datasource.url=jdbc:mysql://$host:$port/wis",
        ).applyTo(applicationContext.environment)
      }
    }
  }
}
