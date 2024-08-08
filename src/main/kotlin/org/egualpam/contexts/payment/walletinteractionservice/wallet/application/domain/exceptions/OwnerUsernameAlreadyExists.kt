package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.OwnerUsername

class OwnerUsernameAlreadyExists(ownerUsername: OwnerUsername) : RuntimeException(
    "Wallet owner with username [${ownerUsername.value}] already exists",
)
