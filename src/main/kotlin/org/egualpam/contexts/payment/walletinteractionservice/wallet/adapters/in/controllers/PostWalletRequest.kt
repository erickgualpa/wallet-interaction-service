package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.command.CreateWalletCommand

data class PostWalletRequest(val wallet: Wallet) {
  data class Wallet(val id: String, val owner: Owner, val account: Account)
  data class Owner(val id: String, val username: String)
  data class Account(val id: String, val currency: String)

  fun toCommand() = CreateWalletCommand(
      wallet.id,
      wallet.owner.id,
      wallet.owner.username,
      wallet.account.id,
      wallet.account.currency,
  )
}
