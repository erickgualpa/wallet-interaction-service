package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEntity

class Account(private val id: AccountId) : DomainEntity {
  override fun getId() = id

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Account

    return id == other.id
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}
