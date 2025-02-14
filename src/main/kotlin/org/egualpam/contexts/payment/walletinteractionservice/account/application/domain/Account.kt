package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AccountCurrency
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot

class Account(
  private val id: AccountId,
  private val currency: AccountCurrency,
  private val deposits: MutableSet<Deposit> = mutableSetOf()
) : AggregateRoot() {
  override fun getId() = id

  companion object {
    fun load(id: String, currency: String, deposits: MutableSet<Deposit>): Account {
      return Account(
          id = AccountId(id),
          currency = AccountCurrency(currency),
          deposits,
      )
    }
  }

  fun depositAmount(depositId: String, amount: Double) {
    Deposit.create(depositId, amount).let {
      this.deposits.add(it)
    }
  }
}
