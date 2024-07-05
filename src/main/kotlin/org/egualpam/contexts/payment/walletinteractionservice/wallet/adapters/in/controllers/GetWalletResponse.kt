package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.WalletDto

data class GetWalletResponse(val wallet: Wallet) {
  data class Wallet(val id: String, val owner: Owner, val account: Account)
  data class Owner(val id: String)
  data class Account(val id: String)

  companion object {
    fun from(walletDto: WalletDto) = GetWalletResponse(
        Wallet(
            walletDto.id,
            Owner(walletDto.owner.id),
            Account(walletDto.account.id),
        ),
    )
  }
}
