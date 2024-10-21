package org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent

interface EventBus {
  fun publish(domainEvents: Set<DomainEvent>)
}
