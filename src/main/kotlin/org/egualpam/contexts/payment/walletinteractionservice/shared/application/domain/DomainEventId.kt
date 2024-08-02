package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain

import java.util.UUID

data class DomainEventId(val value: UUID) {
  companion object {
    fun generate() = DomainEventId(UUID.randomUUID())
  }
}
