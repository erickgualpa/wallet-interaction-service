package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

data class PostWalletRequest(val wallet: Wallet) {
  data class Wallet(val id: String, val owner: Owner, val account: Account)
  data class Owner(val id: String, val username: String)
  data class Account(val id: String, val currency: String)
}
