package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain

abstract class DomainEntity {
  abstract fun getId(): DomainEntityId

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as DomainEntity

    return getId() == other.getId()
  }

  override fun hashCode(): Int {
    return getId().hashCode()
  }
}
