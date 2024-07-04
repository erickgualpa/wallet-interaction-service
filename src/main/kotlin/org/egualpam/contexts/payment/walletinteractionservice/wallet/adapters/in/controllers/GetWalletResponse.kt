package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

data class GetWalletResponse(val wallet: Wallet) {
  data class Wallet(val id: String, val owner: Owner, val account: Account)
  data class Owner(val id: String)
  data class Account(val id: String)
}
