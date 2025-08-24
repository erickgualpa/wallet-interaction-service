package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.TransferMoney
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.CreateAccount
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.DepositMoney
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountApplicationConfiguration {

  @Bean
  fun depositMoney(
    accountRepository: AccountRepository,
    @Qualifier("fakeEventBus") eventBus: EventBus
  ) = DepositMoney(accountRepository, eventBus)

  @Bean
  fun createAccount(
    accountExists: AccountExists,
    accountRepository: AccountRepository,
    @Qualifier("fakeEventBus") eventBus: EventBus
  ): CreateAccount {
    return CreateAccount(accountExists, accountRepository, eventBus)
  }

  @Bean
  fun transferMoney() = TransferMoney()
}
