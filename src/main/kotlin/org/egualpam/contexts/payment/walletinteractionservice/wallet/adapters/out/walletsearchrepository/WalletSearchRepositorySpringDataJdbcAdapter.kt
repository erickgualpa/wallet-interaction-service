package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletsearchrepository

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletCrudRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletSearchRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.WalletDto

class WalletSearchRepositorySpringDataJdbcAdapter(
  private var walletCrudRepository: WalletCrudRepository
) : WalletSearchRepository {
  override fun search(id: WalletId): WalletDto? {
    return walletCrudRepository.findByEntityId(id.value)?.let {
      WalletDto(
          it.entityId,
          WalletDto.OwnerDto(it.ownerId()),
          it.accounts().map { a -> WalletDto.AccountDto(a.entityId) }.toSet(),
      )
    }
  }
}

