package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId

interface WalletExistsPort {
  fun exists(walletId: WalletId): Boolean
}
