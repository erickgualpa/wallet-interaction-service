package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId

interface WalletRepository {
  fun find(walletId: WalletId): Wallet?
  fun save(wallet: Wallet)
}
