package org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository

class DepositMoney(private val repository: AccountRepository) {

  fun execute(command: DepositMoneyCommand) {
    val accountId = AccountId(command.accountId)
    val account = repository.find(accountId) ?: TODO("Not yet implemented")

    account.depositAmount(
        depositId = command.id,
        amount = command.amount,
    )

    repository.save(account)
  }
}

data class DepositMoneyCommand(
  val id: String,
  val amount: Double,
  val currency: String,
  val accountId: String,
)
