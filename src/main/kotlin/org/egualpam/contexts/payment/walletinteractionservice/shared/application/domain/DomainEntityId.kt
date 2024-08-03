package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import java.util.UUID

abstract class DomainEntityId(value: String) {
  init {
    try {
      UUID.fromString(value)
    } catch (e: IllegalArgumentException) {
      throw InvalidDomainEntityId(value)
    }
  }
}
