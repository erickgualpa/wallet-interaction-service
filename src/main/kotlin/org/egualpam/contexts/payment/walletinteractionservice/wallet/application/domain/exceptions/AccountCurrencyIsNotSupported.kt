package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions

class AccountCurrencyIsNotSupported(value: String) : RuntimeException(
    "The provided currency [$value] is not supported",
)
