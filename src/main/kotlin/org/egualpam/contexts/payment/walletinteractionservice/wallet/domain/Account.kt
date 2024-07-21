package org.egualpam.contexts.payment.walletinteractionservice.wallet.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.domain.DomainEntity

class Account(
  private val id: AccountId,
  private val currency: AccountCurrency
) : DomainEntity() {
  override fun getId() = id
}
