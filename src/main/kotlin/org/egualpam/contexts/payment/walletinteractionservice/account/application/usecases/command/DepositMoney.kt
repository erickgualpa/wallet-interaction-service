package org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command

class DepositMoney {

  fun execute(command: DepositMoneyCommand) {}
}

data class DepositMoneyCommand(
  val id: String,
  val amount: Double,
  val currency: String,
  val accountId: String,
)
