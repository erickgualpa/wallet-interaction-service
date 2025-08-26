package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AccountCurrency
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class Account(
  private val id: AccountId,
  private val walletId: AccountWalletId,
  private val currency: AccountCurrency,
  private var balance: AccountBalance,
  private val deposits: MutableSet<Deposit> = mutableSetOf(),
  private val transfers: MutableSet<Transfer> = mutableSetOf(),
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
          balance = AccountBalance(0.0),
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
      balance: AccountBalance,
      deposits: MutableSet<Deposit>,
      transfers: MutableSet<Transfer>,
    ): Account {
      return Account(
          id = AccountId(id),
          walletId = AccountWalletId(walletId),
          currency = AccountCurrency(currency),
          balance,
          deposits,
          transfers,
      )
    }
  }

  fun depositAmount(depositId: String, amount: Double) {
    this.balance = AccountBalance(this.balance.value + amount)

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

  fun transferAmount(
    transferId: String,
    destinationAccountId: String,
    amount: Double
  ) {
    if (amount > balance.value) {
      throw TransferExceedsSourceAccountBalance()
    }

    this.balance = AccountBalance(this.balance.value - amount)

    val transfer = Transfer.create(
        id = transferId,
        sourceAccountId = this.id.value,
        destinationAccountId,
        amount,
        isInbound = false,
    )

    this.transfers.add(transfer)

    val transferProcessed = TransferProcessed(
        DomainEventId.generate(),
        Instant.now(),
        transfer.getId(),
        transfer.sourceAccountId,
        transfer.destinationAccountId,
        transfer.amount,
    )

    addDomainEvent(transferProcessed)
  }

  // TODO: Check what makes more sense, store or not store inbound transfers (right now is redundant)
  fun inboundTransferAmount(
    transferId: String,
    sourceAccountId: String,
    amount: Double
  ) {
    this.balance = AccountBalance(this.balance.value + amount)

    val transfer = Transfer.create(
        id = transferId,
        sourceAccountId,
        destinationAccountId = this.id.value,
        amount,
        isInbound = true,
    )

    this.transfers.add(transfer)
  }

  fun walletId() = walletId
  fun balance() = balance
  fun currency() = currency
  fun deposits() = deposits
  fun transfers() = transfers
}
