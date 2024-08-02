package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.savewallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletPersistenceEntity
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWallet
import org.springframework.transaction.annotation.Transactional

open class SaveWalletSpringDataJdbcAdapter(
  private var walletRepository: WalletRepository
) : SaveWallet {
  @Transactional
  override fun save(wallet: Wallet) {
    WalletPersistenceEntity.from(wallet).let {
      walletRepository.save(it)
    }
  }
}

