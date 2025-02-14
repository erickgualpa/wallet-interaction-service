package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

class AccountNotExists(id: AccountId) : RuntimeException(
    "Account with id [${id.value}] not exists",
)
