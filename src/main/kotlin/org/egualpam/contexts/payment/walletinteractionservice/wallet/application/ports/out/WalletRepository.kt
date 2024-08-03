package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet

interface WalletRepository {
  fun save(wallet: Wallet)
}
