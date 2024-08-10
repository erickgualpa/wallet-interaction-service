package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletCrudRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.WalletDto

class FindWalletSpringDataJdbcAdapter(
  private var walletCrudRepository: WalletCrudRepository
) : FindWallet {
  override fun find(id: WalletId): WalletDto? {
    val results = walletCrudRepository.findByEntityId(id.value)
    return if (results.isEmpty()) {
      null
    } else {
      results.first().let {
        WalletDto(
            it.entityId,
            WalletDto.OwnerDto(it.ownerId()),
            it.accounts().map { a -> WalletDto.AccountDto(a.entityId) }.toSet(),
        )
      }
    }
  }
}

