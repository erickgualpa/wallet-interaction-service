package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.DepositId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletNotExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.DepositExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository

class DepositMoney(
  private val depositExists: DepositExists,
  private val walletRepository: WalletRepository
) {
  fun execute(depositMoneyCommand: DepositMoneyCommand) {
    val depositId = DepositId(depositMoneyCommand.depositId)

    if (depositExists.with(depositId)) {
      return
    }

    val walletId = WalletId(depositMoneyCommand.walletId)
    val wallet = walletRepository.find(walletId) ?: throw WalletNotExists(walletId)
    wallet.depositAmount(
        depositId = depositMoneyCommand.depositId,
        amount = depositMoneyCommand.depositAmount,
        currency = depositMoneyCommand.depositCurrency,
    )
    walletRepository.save(wallet)
  }
}

data class DepositMoneyCommand(
  val walletId: String,
  val depositId: String,
  val depositAmount: Double,
  val depositCurrency: String
)
