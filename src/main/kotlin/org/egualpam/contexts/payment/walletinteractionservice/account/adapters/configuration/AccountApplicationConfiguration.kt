package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command.DepositMoney
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountApplicationConfiguration {

  @Bean("depositMoneyV2")
  fun depositMoney() = DepositMoney()
}
