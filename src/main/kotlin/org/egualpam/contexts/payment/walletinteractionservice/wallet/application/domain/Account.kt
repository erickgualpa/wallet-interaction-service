package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AccountCurrency
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEntity

class Account private constructor(
  private val id: AccountId,
  private val currency: AccountCurrency,
) : DomainEntity() {
  companion object {
    fun create(
      id: String,
      currency: String,
    ) = Account(
        AccountId(id),
        AccountCurrency(currency),
    )

    fun load(
      id: String,
      currency: String,
    ) = Account(
        AccountId(id),
        AccountCurrency(currency),
    )
  }

  override fun getId() = id

  fun getCurrency() = currency
}
