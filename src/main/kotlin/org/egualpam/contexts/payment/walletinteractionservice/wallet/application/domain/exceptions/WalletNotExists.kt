package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId

class WalletNotExists(
  walletId: WalletId
) : RuntimeException("Wallet with id [${walletId.value}] not exists")
