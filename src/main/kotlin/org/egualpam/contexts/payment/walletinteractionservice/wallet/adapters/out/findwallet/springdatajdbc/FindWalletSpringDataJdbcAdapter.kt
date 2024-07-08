package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.springdatajdbc

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Owner
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.OwnerId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId

class FindWalletSpringDataJdbcAdapter(
  private var walletRepository: WalletRepository
) : FindWalletPort {
  override fun find(id: WalletId): Wallet? {
    val wallet: WalletPersistenceEntity? = walletRepository.findById(id.value).orElse(null)
    return wallet?.let {
      Wallet(
          WalletId(it.id),
          Owner(OwnerId(it.ownerId)),
          Account(AccountId(it.accountId)),
      )
    }
  }
}

