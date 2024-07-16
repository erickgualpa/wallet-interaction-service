package org.egualpam.contexts.payment.walletinteractionservice.wallet.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.domain.AggregateRoot

class Wallet(
  private val id: WalletId,
  private val owner: Owner,
  private val account: Account
) : AggregateRoot {

  companion object {
    fun create(
      id: String,
      ownerId: String,
      ownerUsername: String,
      accountId: String,
      accountCurrency: String
    ): Wallet {
      val walletId = WalletId(id)
      val owner = Owner(
          OwnerId(ownerId),
          OwnerUsername(ownerUsername),
      )
      val account = Account(
          AccountId(accountId),
          AccountCurrency(accountCurrency),
      )
      return Wallet(walletId, owner, account)
    }
  }

  fun getAccountId() = account.getId()

  fun getOwnerId() = owner.getId()

  fun getOwnerUsername() = owner.getUsername()

  override fun getId() = id

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Wallet

    return id == other.id
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}
