package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId

interface FindWalletPort {
  fun find(id: WalletId): Wallet?
}
