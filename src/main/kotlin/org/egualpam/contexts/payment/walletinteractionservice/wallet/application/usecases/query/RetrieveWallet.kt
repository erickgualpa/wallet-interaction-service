package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletNotExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletSearchRepository

class RetrieveWallet(private val walletSearchRepository: WalletSearchRepository) {
  fun execute(retrieveWalletQuery: RetrieveWalletQuery): WalletDto {
    val walletId = WalletId(retrieveWalletQuery.id)
    return walletSearchRepository.search(walletId) ?: throw WalletNotExists(walletId)
  }
}

data class RetrieveWalletQuery(val id: String)

data class WalletDto(
  val id: String,
  val owner: OwnerDto,
  val accounts: Set<AccountDto>
) {
  data class OwnerDto(val id: String)
  data class AccountDto(val id: String)
}
