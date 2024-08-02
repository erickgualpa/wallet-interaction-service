package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot

class Wallet(
  id: String,
  ownerId: String,
  ownerUsername: String,
  accountId: String,
  accountCurrency: String
) : AggregateRoot() {

  private val id: WalletId
  private val owner: Owner
  private val account: Account

  init {
    this.id = WalletId(id)
    this.owner = Owner(
        OwnerId(ownerId),
        OwnerUsername(ownerUsername),
    )
    this.account = Account(
        AccountId(accountId),
        AccountCurrency(accountCurrency),
    )
    this.domainEvents.add(WalletCreated(this))
  }

  override fun getId() = id

  fun getAccountId() = account.getId()

  fun getOwnerId() = owner.getId()

  fun getOwnerUsername() = owner.getUsername()

  fun getAccountCurrency() = account.getCurrency()
}
