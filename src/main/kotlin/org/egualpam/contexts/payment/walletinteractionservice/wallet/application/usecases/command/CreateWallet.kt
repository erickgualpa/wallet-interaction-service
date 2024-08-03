package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletAlreadyExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists

class CreateWallet(
  private val walletExists: WalletExists,
  private val saveWallet: SaveWallet
) {
  fun execute(createWalletCommand: CreateWalletCommand) {
    try {
      val wallet = Wallet.create(
          createWalletCommand.walletId,
          createWalletCommand.ownerId,
          createWalletCommand.ownerUsername,
          createWalletCommand.accountId,
          createWalletCommand.accountCurrency,
          walletExists,
      )
      saveWallet.save(wallet)
    } catch (_: WalletAlreadyExists) {
    }
  }
}

data class CreateWalletCommand(
  val walletId: String,
  val ownerId: String,
  val ownerUsername: String,
  val accountId: String,
  val accountCurrency: String
)
