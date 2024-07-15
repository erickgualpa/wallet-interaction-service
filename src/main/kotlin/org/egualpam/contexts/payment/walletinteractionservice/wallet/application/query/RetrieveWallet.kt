package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.query

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.exceptions.WalletNotExists

class RetrieveWallet(private var findWalletPort: FindWalletPort) {
  fun execute(retrieveWalletQuery: RetrieveWalletQuery): WalletDto {
    val walletId = WalletId(retrieveWalletQuery.id)
    return findWalletPort.find(walletId) ?: throw WalletNotExists(walletId)
  }
}

data class RetrieveWalletQuery(val id: String)

data class WalletDto(val id: String, val owner: OwnerDto, val account: AccountDto) {
  data class OwnerDto(val id: String)
  data class AccountDto(val id: String)
}
