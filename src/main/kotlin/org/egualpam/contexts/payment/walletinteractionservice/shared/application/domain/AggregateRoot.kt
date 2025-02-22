package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain

abstract class AggregateRoot {

  protected val domainEvents = mutableSetOf<DomainEvent>()

  fun pullDomainEvents(): Set<DomainEvent> {
    val domainEventsCopy = domainEvents.toSet()
    domainEvents.clear()
    return domainEventsCopy
  }

  abstract fun getId(): AggregateId

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as AggregateRoot

    return getId() == other.getId()
  }

  override fun hashCode(): Int {
    return getId().hashCode()
  }
}
