package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEntity

class Account private constructor(
  private val id: AccountId,
  private val currency: AccountCurrency,
  private val deposits: MutableSet<Deposit> = mutableSetOf()
) : DomainEntity() {
  companion object {
    fun create(
      id: String,
      currency: String,
      deposits: MutableSet<Deposit> = mutableSetOf()
    ) = Account(
        AccountId(id),
        AccountCurrency(currency),
        deposits,
    )

    fun load(
      id: String,
      currency: String,
      deposits: MutableSet<Deposit> = mutableSetOf()
    ) = Account(
        AccountId(id),
        AccountCurrency(currency),
        deposits,
    )
  }

  override fun getId() = id

  fun getCurrency() = currency

  fun deposits(): Set<Deposit> = deposits

  fun depositAmount(depositId: String, amount: Double) {
    Deposit.create(depositId, amount).let {
      this.deposits.add(it)
    }
  }
}
