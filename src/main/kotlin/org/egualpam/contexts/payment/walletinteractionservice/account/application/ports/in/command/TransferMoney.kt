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

    accountRepository.save(sourceAccount)
    accountRepository.save(destinationAccount)
  }
}

data class TransferMoneyCommand(
  val transferId: String,
  val sourceAccountId: String,
  val destinationAccountId: String,
  val amount: Double
)
