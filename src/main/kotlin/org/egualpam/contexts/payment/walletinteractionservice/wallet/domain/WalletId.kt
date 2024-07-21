package org.egualpam.contexts.payment.walletinteractionservice.wallet.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.domain.AggregateId
import org.egualpam.contexts.payment.walletinteractionservice.shared.domain.exceptions.InvalidDomainEntityId
import java.util.UUID

data class WalletId(val value: String) : AggregateId {
  init {
    try {
      UUID.fromString(value)
    } catch (e: IllegalArgumentException) {
      throw InvalidDomainEntityId()
    }
  }
}
