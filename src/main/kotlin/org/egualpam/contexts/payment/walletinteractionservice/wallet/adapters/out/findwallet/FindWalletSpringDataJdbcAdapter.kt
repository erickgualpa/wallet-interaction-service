package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.query.WalletDto
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId

class FindWalletSpringDataJdbcAdapter(
  private var walletRepository: WalletRepository
) : FindWalletPort {
  override fun find(id: WalletId): WalletDto? {
    val results = walletRepository.findByEntityId(id.value)
    return if (results.isEmpty()) {
      null
    } else {
      results.first().let {
        WalletDto(
            it.entityId,
            WalletDto.OwnerDto(it.ownerId),
            WalletDto.AccountDto(it.accountId),
        )
      }
    }
  }
}

