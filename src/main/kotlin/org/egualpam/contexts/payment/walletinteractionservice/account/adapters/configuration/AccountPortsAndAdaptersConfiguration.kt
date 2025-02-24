package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.stream.Environment
import org.egualpam.contexts.payment.walletinteractionservice.account.adapters.`in`.consumers.CreateAccountConsumer
import org.egualpam.contexts.payment.walletinteractionservice.account.adapters.out.accountrepository.springjdbccore.SpringJdbcCoreAccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.CreateAccount
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.support.TransactionTemplate

@Configuration
class AccountPortsAndAdaptersConfiguration {
  @Bean
  fun accountRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
  ): AccountRepository = SpringJdbcCoreAccountRepository(jdbcTemplate)

  @Bean
  fun createAccountConsumer(
    rabbitMqEnvironment: Environment,
    objectMapper: ObjectMapper,
    transactionTemplate: TransactionTemplate,
    createAccount: CreateAccount
  ) = CreateAccountConsumer(
      rabbitMqEnvironment,
      objectMapper,
      transactionTemplate,
      createAccount,
  )
}
