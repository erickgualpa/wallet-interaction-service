package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletCrudRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletPersistenceEntity
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository

open class WalletRepositorySpringDataJdbcAdapter(
  private var walletRepository: WalletCrudRepository
) : WalletRepository {
  override fun save(wallet: Wallet) {
    WalletPersistenceEntity.from(wallet).let {
      walletRepository.save(it)
    }
  }
}

