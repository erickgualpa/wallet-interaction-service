package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.CreateAccount
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.DepositMoney
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.TransferMoney
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID.randomUUID

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
  fun transferMoney(): TransferMoney {
    val fakeRepository = object : AccountRepository {
      override fun find(id: AccountId) = Account.load(
          id = id.value,
          currency = "EUR",
          walletId = randomUUID().toString(),
          deposits = mutableSetOf(),
      )

      override fun save(account: Account) {
      }
    }
    return TransferMoney(fakeRepository)
  }
}
