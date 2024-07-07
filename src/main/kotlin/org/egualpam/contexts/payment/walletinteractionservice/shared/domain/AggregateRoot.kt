package org.egualpam.contexts.payment.walletinteractionservice.shared.domain

interface AggregateRoot {
  fun getId(): AggregateId
}
