package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.DepositId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletNotExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.DepositExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository

class DepositMoney(
  private val depositExists: DepositExists,
  private val walletRepository: WalletRepository,
  private val eventBus: EventBus
) {
  fun execute(depositMoneyCommand: DepositMoneyCommand) {
    val depositId = DepositId(depositMoneyCommand.depositId)

    if (depositExists.with(depositId)) {
      return
    }

    val walletId = WalletId(depositMoneyCommand.walletId)
    val wallet = walletRepository.find(walletId) ?: throw WalletNotExists(walletId)

    depositMoneyCommand.let {
      wallet.depositAmount(
          depositId = it.depositId,
          amount = it.depositAmount,
          currency = it.depositCurrency,
      )
    }

    walletRepository.save(wallet)
    eventBus.publish(wallet.pullDomainEvents())
  }
}

data class DepositMoneyCommand(
  val walletId: String,
  val depositId: String,
  val depositAmount: Double,
  val depositCurrency: String
)
