package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId

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

      wallet.domainEvents.add(WalletCreated(domainEventId, wallet))
      return wallet
    }
  }

  fun depositAmount(depositId: String, amount: Double, currency: String) {
    val accountCurrency = AccountCurrency(currency)
    this.accounts.first { it.getCurrency() == accountCurrency }.depositAmount(
        depositId,
        amount,
    )
    this.domainEvents.add(DepositProcessed(this))
  }

  override fun getId() = id

  fun getOwnerId() = owner.getId()

  fun getOwnerUsername() = owner.getUsername()

  fun accounts(): Set<Account> = accounts
}
