package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain

interface EventBus {
  fun publish(domainEvents: Set<DomainEvent>)
}
