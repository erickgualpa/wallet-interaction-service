package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot

class Wallet(
  id: String,
  ownerId: String,
  ownerUsername: String,
  accountId: String,
  accountCurrency: String
) : AggregateRoot() {

  private val id: WalletId = WalletId(id)
  private val owner: Owner = Owner(
      OwnerId(ownerId),
      OwnerUsername(ownerUsername),
  )
  private val account: Account = Account(
      AccountId(accountId),
      AccountCurrency(accountCurrency),
  )

  init {
    this.domainEvents.add(WalletCreated(this))
  }

  override fun getId() = id

  fun getAccountId() = account.getId()

  fun getOwnerId() = owner.getId()

  fun getOwnerUsername() = owner.getUsername()

  fun getAccountCurrency() = account.getCurrency()
}
