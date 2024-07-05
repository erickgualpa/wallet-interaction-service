package org.egualpam.contexts.payment.walletinteractionservice.wallet.application

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletNotExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort

class RetrieveWallet(private var findWalletPort: FindWalletPort) {
  fun execute(retrieveWalletQuery: RetrieveWalletQuery): WalletDto {
    val walletId = WalletId(retrieveWalletQuery.id)
    val wallet = findWalletPort.find(walletId) ?: throw WalletNotExists(walletId)
    return WalletDto.from(wallet)
  }
}

data class RetrieveWalletQuery(val id: String)

data class WalletDto(val id: String, val owner: OwnerDto, val account: AccountDto) {
  data class OwnerDto(val id: String)
  data class AccountDto(val id: String)

  companion object {
    fun from(wallet: Wallet) = WalletDto(
        wallet.getId().value,
        OwnerDto(wallet.getOwnerId().value),
        AccountDto(wallet.getAccountId().value),
    )
  }
}
