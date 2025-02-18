package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.AccountTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.DepositTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.OwnerTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.WalletStreamTestConsumer
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.WalletTestRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
@EnableConfigurationProperties(RabbitMqProperties::class)
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

  @Bean
  fun walletStreamTestConsumer(
    rabbitMqProperties: RabbitMqProperties,
    objectMapper: ObjectMapper
  ) = WalletStreamTestConsumer(rabbitMqProperties)
}
