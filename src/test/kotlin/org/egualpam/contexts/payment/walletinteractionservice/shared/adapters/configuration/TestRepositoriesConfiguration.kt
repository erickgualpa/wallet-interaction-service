package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.AccountTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.DepositTestRepository
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.OwnerTestRepository
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
}
