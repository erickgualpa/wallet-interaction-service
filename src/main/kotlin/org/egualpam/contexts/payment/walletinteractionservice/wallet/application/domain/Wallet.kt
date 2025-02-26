package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AccountCurrency
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AggregateRoot
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class Wallet(
  private val id: WalletId,
  private val owner: Owner
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

      val walletCreated = WalletCreated(
          id = domainEventId,
          walletId = wallet.id,
          occurredOn = Instant.now(),
          ownerId = wallet.owner.getId(),
          ownerUsername = wallet.owner.getUsername(),
          // TODO: This should not be here but generated after. 'AccountCreated' event could consumed to know this parameters
          accountId = AccountId(accountId),
          accountCurrency = AccountCurrency(accountCurrency),
      )
      wallet.domainEvents.add(walletCreated)
      return wallet
    }
  }

  override fun getId() = id

  fun getOwnerId() = owner.getId()

  fun getOwnerUsername() = owner.getUsername()
}
