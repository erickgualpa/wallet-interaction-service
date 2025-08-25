package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AccountCurrency
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class Account(
  private val id: AccountId,
  private val walletId: AccountWalletId,
  private val currency: AccountCurrency,
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
      deposits: MutableSet<Deposit>,
      transfers: MutableSet<Transfer>,
    ): Account {
      return Account(
          id = AccountId(id),
          walletId = AccountWalletId(walletId),
          currency = AccountCurrency(currency),
          deposits,
          transfers,
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

  fun transferAmount(
    transferId: String,
    destinationAccountId: String,
    amount: Double
  ) {
    val transfer = Transfer.create(
        id = transferId,
        sourceAccountId = this.id.value,
        destinationAccountId,
        amount,
        isInbound = false,
    )
    this.transfers.add(transfer)
  }

  fun inboundTransferAmount(
    transferId: String,
    sourceAccountId: String,
    amount: Double
  ) {
    val transfer = Transfer.create(
        id = transferId,
        sourceAccountId,
        destinationAccountId = this.id.value,
        amount,
        isInbound = true,
    )
    this.transfers.add(transfer)
  }

  // TODO: Update this workaround (currently used to have  some 'balance' view)
  fun balance(): AccountBalance {
    val depositsSum =
        if (deposits.isEmpty()) 0.0
        else deposits.sumOf { it.amount().value }


    val outboundTransfers = transfers.filter { it.isOutbound() }
    val outboundTransfersSum =
        if (outboundTransfers.isEmpty()) 0.0
        else outboundTransfers.sumOf { it.amount().value }

    val inboundTransfers = transfers.filter { it.isInbound() }
    val inboundTransfersSum =
        if (inboundTransfers.isEmpty()) 0.0
        else inboundTransfers.sumOf { it.amount().value }

    return AccountBalance(
        depositsSum
            - outboundTransfersSum
            + inboundTransfersSum,
    )
  }


  fun walletId() = walletId
  fun currency() = currency
  fun deposits() = deposits
  fun transfers() = transfers
}
