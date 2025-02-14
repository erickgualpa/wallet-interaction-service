package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEntityId

data class AccountWalletId(val value: String) : DomainEntityId(value)
