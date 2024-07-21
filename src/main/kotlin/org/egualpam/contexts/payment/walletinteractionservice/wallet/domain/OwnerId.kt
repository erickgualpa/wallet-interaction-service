package org.egualpam.contexts.payment.walletinteractionservice.wallet.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.domain.DomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.shared.domain.exceptions.InvalidDomainEntityId
import java.util.UUID

data class OwnerId(val value: String) : DomainEntityId {
  init {
    try {
      UUID.fromString(value)
    } catch (e: IllegalArgumentException) {
      throw InvalidDomainEntityId()
    }
  }
}
