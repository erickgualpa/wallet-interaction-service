package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.savewallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletPersistenceEntity
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWalletPort
import org.springframework.transaction.annotation.Transactional

open class SaveWalletSpringDataJdbcAdapter(
  private var walletRepository: WalletRepository
) : SaveWalletPort {
  @Transactional
  override fun save(wallet: Wallet) {
    val walletPersistenceEntity = WalletPersistenceEntity.from(wallet)
    walletRepository.save(walletPersistenceEntity)
  }
}

