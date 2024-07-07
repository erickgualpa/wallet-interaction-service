package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.ComposeContainer
import java.io.File

@AutoConfigureMockMvc
@ContextConfiguration(initializers = [AbstractIntegrationTest.Companion.MySQLInitializer::class])
@ActiveProfiles("integration-test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [WalletInteractionServiceApplication::class],
)
abstract class AbstractIntegrationTest {

  @Autowired
  protected lateinit var mockMvc: MockMvc

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
