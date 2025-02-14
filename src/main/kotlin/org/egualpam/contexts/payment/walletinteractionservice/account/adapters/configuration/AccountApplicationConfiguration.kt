package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command.DepositMoney
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountApplicationConfiguration {

  @Bean("depositMoneyV2")
  fun depositMoney(
    accountRepository: AccountRepository,
    eventBus: EventBus
  ) = DepositMoney(accountRepository, eventBus)
}
