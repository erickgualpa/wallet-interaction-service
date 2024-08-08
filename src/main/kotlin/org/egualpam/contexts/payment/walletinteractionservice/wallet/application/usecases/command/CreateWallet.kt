package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.OwnerUsername
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.OwnerUsernameAlreadyExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository

class CreateWallet(
  private val walletExists: WalletExists,
  private val walletRepository: WalletRepository
) {
  fun execute(createWalletCommand: CreateWalletCommand) {
    val walletId = WalletId(createWalletCommand.walletId)
    if (walletExists.with(walletId)) {
      return
    }

    val ownerUsername = OwnerUsername(createWalletCommand.ownerUsername)
    if (walletExists.with(ownerUsername)) {
      throw OwnerUsernameAlreadyExists(ownerUsername)
    }

    val wallet = Wallet.create(
        createWalletCommand.walletId,
        createWalletCommand.ownerId,
        createWalletCommand.ownerUsername,
        createWalletCommand.accountId,
        createWalletCommand.accountCurrency,
    )

    walletRepository.save(wallet)
  }
}

data class CreateWalletCommand(
  val walletId: String,
  val ownerId: String,
  val ownerUsername: String,
  val accountId: String,
  val accountCurrency: String
)
