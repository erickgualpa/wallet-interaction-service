package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.account.adapters.out.accountrepository.springjdbccore.SpringJdbcCoreAccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class AccountPortsAndAdaptersConfiguration {
  @Bean
  fun accountRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
  ): AccountRepository = SpringJdbcCoreAccountRepository(jdbcTemplate)
}
