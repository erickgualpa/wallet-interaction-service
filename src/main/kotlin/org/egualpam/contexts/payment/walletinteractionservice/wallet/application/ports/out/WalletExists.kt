package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.OwnerUsername
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId

interface WalletExists {
  fun with(walletId: WalletId): Boolean

  fun with(ownerUsername: OwnerUsername): Boolean
}
