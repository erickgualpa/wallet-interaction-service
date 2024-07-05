package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain

interface AggregateRoot {
  fun getId(): AggregateId
}
