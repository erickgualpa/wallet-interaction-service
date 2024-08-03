package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletAlreadyExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists

class Wallet private constructor(
  private val id: WalletId,
  private val owner: Owner,
  private val account: Account
) : AggregateRoot() {

  companion object {
    fun create(
      id: String,
      ownerId: String,
      ownerUsername: String,
      accountId: String,
      accountCurrency: String,
      walletExists: WalletExists
    ): Wallet {
      val walletId = WalletId(id)

      if (walletExists.with(walletId)) {
        throw WalletAlreadyExists(walletId)
      }

      val wallet = Wallet(
          walletId,
          Owner(
              OwnerId(ownerId),
              OwnerUsername(ownerUsername),
          ),
          Account(
              AccountId(accountId),
              AccountCurrency(accountCurrency),
          ),
      )

      wallet.domainEvents.add(WalletCreated(wallet))

      return wallet
    }
  }

  override fun getId() = id

  fun getAccountId() = account.getId()

  fun getOwnerId() = owner.getId()

  fun getOwnerUsername() = owner.getUsername()

  fun getAccountCurrency() = account.getCurrency()
}
