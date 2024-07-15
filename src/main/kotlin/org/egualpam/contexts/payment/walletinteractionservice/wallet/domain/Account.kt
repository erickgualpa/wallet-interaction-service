package org.egualpam.contexts.payment.walletinteractionservice.wallet.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.domain.DomainEntity

class Account(
  private val id: AccountId,
  private val currency: AccountCurrency
) : DomainEntity {
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
