package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot

class Account(private val id: AccountId) : AggregateRoot() {
  override fun getId() = id

  companion object {
    fun load(id: String): Account {
      return Account(
          id = AccountId(id),
      )
    }
  }
}
