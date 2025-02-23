package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.shared

import java.time.Instant

data class PublicEvent(
  val id: String,
  val type: String,
  val version: String,
  val aggregateId: String,
  val occurredOn: Instant,
  val data: PublicEventData
)
