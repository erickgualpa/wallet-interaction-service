package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEntity

class Account(
  private val id: AccountId,
  private val currency: AccountCurrency
) : DomainEntity() {
  override fun getId() = id

  fun getCurrency() = currency
}
