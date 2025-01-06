package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain

@FunctionalInterface
interface DomainEventIdSupplier {
  fun get(): DomainEventId
}
