package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.springdatajdbc

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletPersistenceEntity
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.query.WalletDto
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId

class FindWalletSpringDataJdbcAdapter(
  private var walletRepository: WalletRepository
) : FindWalletPort {
  override fun find(id: WalletId): WalletDto? {
    val wallet: WalletPersistenceEntity? = walletRepository.findById(id.value).orElse(null)
    return wallet?.let {
      WalletDto(
          it.entityId,
          WalletDto.OwnerDto(it.ownerId),
          WalletDto.AccountDto(it.accountId),
      )
    }
  }
}

