package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`

class TransferMoney {
  fun execute(command: TransferMoneyCommand) {}
}

data class TransferMoneyCommand(
  val transferId: String,
  val sourceAccountId: String,
  val destinationAccountId: String,
  val amount: Double
)
