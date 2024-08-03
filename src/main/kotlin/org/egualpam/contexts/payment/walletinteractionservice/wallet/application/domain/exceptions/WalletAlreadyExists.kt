package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId

class WalletAlreadyExists(walletId: WalletId) : RuntimeException(
    "Wallet already exists with id [${walletId.value}]",
)
