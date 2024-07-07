package org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.exceptions

import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId

class WalletNotExists(
  walletId: WalletId
) : RuntimeException("Wallet with id [${walletId.value}] not exists")
