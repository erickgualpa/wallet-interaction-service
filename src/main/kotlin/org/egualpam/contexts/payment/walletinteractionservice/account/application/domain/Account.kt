package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AccountCurrency
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class Account(
  private val id: AccountId,
  private val walletId: AccountWalletId,
  private val currency: AccountCurrency,
  private val deposits: MutableSet<Deposit> = mutableSetOf()
) : AggregateRoot() {
  override fun getId() = id

  companion object {
    fun create(
      id: String,
      walletId: String,
      currency: String,
    ): Account {
      val account = Account(
          id = AccountId(id),
          walletId = AccountWalletId(walletId),
          currency = AccountCurrency(currency),
      )

      val event = AccountCreated(
          id = DomainEventId.generate(),
          accountId = account.id,
          occurredOn = Instant.now(),
      )

      account.addDomainEvent(event)

      return account
    }

    fun load(
      id: String,
      walletId: String,
      currency: String,
      deposits: MutableSet<Deposit>
    ): Account {
      return Account(
          id = AccountId(id),
          walletId = AccountWalletId(walletId),
          currency = AccountCurrency(currency),
          deposits,
      )
    }
  }

  fun depositAmount(depositId: String, amount: Double) {
    val deposit = Deposit.create(depositId, amount)
    this.deposits.add(deposit)

    val depositProcessed = DepositProcessed(
        DomainEventId.generate(),
        this.id,
        Instant.now(),
        deposit.getId(),
        deposit.amount(),
        this.currency,
    )

    this.domainEvents.add(depositProcessed)
  }

  fun walletId() = walletId
  fun currency() = currency
  fun deposits() = deposits
}
