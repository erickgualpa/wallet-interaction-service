package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus

class CreateAccount(
  private val accountExists: AccountExists,
  private val repository: AccountRepository,
  private val eventBus: EventBus
) {
  fun execute(command: CreateAccountCommand) {
    val accountId = AccountId(command.accountId)

    if (accountExists.with(accountId)) {
      return
    }

    val account = Account.create(
        id = accountId.value,
        walletId = command.walletId,
        currency = command.accountCurrency,
    )

    repository.save(account)
    eventBus.publish(account.pullDomainEvents())
  }
}

data class CreateAccountCommand(
  val accountId: String,
  val accountCurrency: String,
  val walletId: String
)
