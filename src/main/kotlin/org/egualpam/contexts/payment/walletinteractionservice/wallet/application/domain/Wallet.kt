package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot

class Wallet private constructor(
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
      accountCurrency: String
    ): Wallet {
      val account = Account(
          AccountId(accountId),
          AccountCurrency(accountCurrency),
      )
      val wallet = Wallet(
          WalletId(id),
          Owner(
              OwnerId(ownerId),
              OwnerUsername(ownerUsername),
          ),
      )
      wallet.accounts.add(account)
      wallet.domainEvents.add(WalletCreated(wallet))
      return wallet
    }
  }

  override fun getId() = id

  fun getOwnerId() = owner.getId()

  fun getOwnerUsername() = owner.getUsername()

  fun accounts(): Set<Account> = accounts
}
