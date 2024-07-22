package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet

interface SaveWalletPort {
  fun save(wallet: Wallet)
}
