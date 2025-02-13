package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command.DepositMoney
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountApplicationConfiguration {

  @Bean("depositMoneyV2")
  fun depositMoney(): DepositMoney {
    val fakeRepository = object : AccountRepository {
      override fun find(id: AccountId): Account? = null
      override fun save(account: Account) {
      }
    }
    return DepositMoney(fakeRepository)
  }
}
