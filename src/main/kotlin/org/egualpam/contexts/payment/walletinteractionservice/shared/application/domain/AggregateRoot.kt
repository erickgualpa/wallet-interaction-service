package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain

abstract class AggregateRoot {
  // TODO: Make domainEvents a Set
  protected val domainEvents = arrayListOf<DomainEvent>()

  fun pullDomainEvents(): Set<DomainEvent> {
    val domainEventsCopy = ArrayList(domainEvents)
    domainEvents.clear()
    return domainEventsCopy.toSet()
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
