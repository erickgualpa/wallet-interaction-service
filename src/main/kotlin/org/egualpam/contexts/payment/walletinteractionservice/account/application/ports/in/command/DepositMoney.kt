package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountNotExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus

class DepositMoney(
  private val repository: AccountRepository,
  private val eventBus: EventBus
) {
  fun execute(command: DepositMoneyCommand) {
    val accountId = AccountId(command.accountId)
    val account = repository.find(accountId) ?: throw AccountNotExists(accountId)

    account.depositAmount(
        depositId = command.id,
        amount = command.amount,
    )

    repository.save(account)
    eventBus.publish(account.pullDomainEvents())
  }
}

data class DepositMoneyCommand(
  val id: String,
  val amount: Double,
  val currency: String,
  val accountId: String,
)
