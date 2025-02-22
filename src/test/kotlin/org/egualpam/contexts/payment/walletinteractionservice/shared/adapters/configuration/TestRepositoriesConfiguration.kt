package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.stream.Address
import com.rabbitmq.stream.Environment
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.AccountTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.DepositTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.OwnerTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.WalletStreamTestConsumer
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.WalletTestRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class TestRepositoriesConfiguration {
  @Bean
  fun walletTestRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
  ) = WalletTestRepository(jdbcTemplate)

  @Bean
  fun ownerTestRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
  ) = OwnerTestRepository(jdbcTemplate)

  @Bean
  fun accountTestRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
  ) = AccountTestRepository(jdbcTemplate)

  @Bean
  fun depositTestRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
  ) = DepositTestRepository(jdbcTemplate)

  // TODO: Move into 'TestConsumersConfiguration'
  @Bean
  fun walletStreamTestConsumer(
    rabbitMqProperties: RabbitMqProperties,
    objectMapper: ObjectMapper
  ): WalletStreamTestConsumer {
    val environment = Environment.builder()
        .addressResolver {
          Address(rabbitMqProperties.host, rabbitMqProperties.streamPort)
        }
        .username(rabbitMqProperties.adminUsername)
        .password(rabbitMqProperties.adminPassword)
        .build()

    return WalletStreamTestConsumer(environment)
  }
}
