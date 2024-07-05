package org.egualpam.contexts.payment.walletinteractionservice.wallet.application

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet

class RetrieveWallet {
  fun execute(retrieveWalletQuery: RetrieveWalletQuery): WalletDto {
    val wallet = Wallet.create(
        retrieveWalletQuery.id,
        ownerId = "fake-owner-id",
        accountId = "fake-account-id",
    )
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
