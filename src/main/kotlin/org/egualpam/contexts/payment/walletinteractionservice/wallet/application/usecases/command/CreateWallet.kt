package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWalletPort

class CreateWallet(
  private val saveWalletPort: SaveWalletPort
) {
  fun execute(createWalletCommand: CreateWalletCommand) {
    val wallet = createWalletCommand.let {
      Wallet.create(
          it.walletId,
          it.ownerId,
          it.ownerUsername,
          it.accountId,
          it.accountCurrency,
      )
    }
    saveWalletPort.save(wallet)
  }
}

data class CreateWalletCommand(
  val walletId: String,
  val ownerId: String,
  val ownerUsername: String,
  val accountId: String,
  val accountCurrency: String
)