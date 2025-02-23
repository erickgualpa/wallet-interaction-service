package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidAggregateId
import java.util.UUID

open class AggregateId(val value: String) {
  init {
    try {
      UUID.fromString(value)
    } catch (e: IllegalArgumentException) {
      throw InvalidAggregateId(value)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as AggregateId

    return value == other.value
  }

  override fun hashCode(): Int {
    return value.hashCode()
  }
}
