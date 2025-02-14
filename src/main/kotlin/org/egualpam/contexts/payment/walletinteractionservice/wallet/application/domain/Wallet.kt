package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AccountCurrency
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class Wallet(
  private val id: WalletId,
  private val owner: Owner,
  private val accounts: MutableSet<Account> = mutableSetOf()
) : AggregateRoot() {

  companion object {
    fun create(
      id: String,
      ownerId: String,
      ownerUsername: String,
      accountId: String,
      accountCurrency: String,
      domainEventId: DomainEventId
    ): Wallet {
      val wallet = Wallet(
          WalletId(id),
          Owner.create(ownerId, ownerUsername),
      )
      val account = Account.create(accountId, accountCurrency)
      wallet.accounts.add(account)

      val walletCreated = WalletCreated(
          id = domainEventId,
          walletId = wallet.id,
          occurredOn = Instant.now(),
          ownerId = wallet.owner.getId(),
          ownerUsername = wallet.owner.getUsername(),
          accountId = wallet.accounts.first().getId(),
          accountCurrency = wallet.accounts.first().getCurrency(),
      )
      wallet.domainEvents.add(walletCreated)
      return wallet
    }
  }

  fun depositAmount(
    depositId: String,
    amount: Double,
    currency: String,
    domainEventId: DomainEventId
  ) {
    val accountCurrency = AccountCurrency(currency)
    val account = this.accounts.first { it.getCurrency() == accountCurrency }

    account.depositAmount(
        depositId,
        amount,
    )

    val depositProcessed = DepositProcessed(
        id = domainEventId,
        walletId = this.id,
        occurredOn = Instant.now(),
        depositId = DepositId(depositId),
        amount = DepositAmount(amount),
        accountCurrency,
    )
    this.domainEvents.add(depositProcessed)
  }

  override fun getId() = id

  fun getOwnerId() = owner.getId()

  fun getOwnerUsername() = owner.getUsername()

  fun accounts(): Set<Account> = accounts
}
