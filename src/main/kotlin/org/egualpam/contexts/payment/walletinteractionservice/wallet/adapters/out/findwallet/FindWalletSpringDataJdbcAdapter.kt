package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.WalletDto

class FindWalletSpringDataJdbcAdapter(
  private var walletRepository: WalletRepository
) : FindWallet {
  override fun find(id: WalletId): WalletDto? {
    val results = walletRepository.findByEntityId(id.value)
    return if (results.isEmpty()) {
      null
    } else {
      results.first().let {
        WalletDto(
            it.entityId,
            WalletDto.OwnerDto(it.ownerId()),
            WalletDto.AccountDto(it.accountId()),
        )
      }
    }
  }
}

