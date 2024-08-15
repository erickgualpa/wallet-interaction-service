package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain;

import java.time.Instant

abstract class DomainEvent(private val aggregateRoot: AggregateRoot) {
  abstract fun id(): DomainEventId

  abstract fun occurredOn(): Instant

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as DomainEvent

    return id() == other.id()
  }

  override fun hashCode(): Int {
    return id().hashCode()
  }
}
