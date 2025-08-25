package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountNotExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository

class TransferMoney(
  private val accountRepository: AccountRepository
) {
  fun execute(command: TransferMoneyCommand) {
    val sourceAccountId = AccountId(command.sourceAccountId)
    val destinationAccountId = AccountId(command.destinationAccountId)

    val sourceAccount =
        accountRepository.find(sourceAccountId)
          ?: throw AccountNotExists(sourceAccountId)

    val destinationAccount =
        accountRepository.find(destinationAccountId)
          ?: throw AccountNotExists(destinationAccountId)

    sourceAccount.transferAmount(
        transferId = command.transferId,
        destinationAccountId = destinationAccountId.value,
        amount = command.amount,
    )

    destinationAccount.inboundTransferAmount(
        transferId = command.transferId,
        sourceAccountId = sourceAccountId.value,
        amount = command.amount,
    )

    accountRepository.save(sourceAccount)
    accountRepository.save(destinationAccount)

    // TODO: Publish domain events
  }
}

data class TransferMoneyCommand(
  val transferId: String,
  val sourceAccountId: String,
  val destinationAccountId: String,
  val amount: Double
)
