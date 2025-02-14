package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AccountCurrency
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class WalletCreated(
  id: DomainEventId,
  walletId: WalletId,
  occurredOn: Instant,
  private val ownerId: OwnerId,
  private val ownerUsername: OwnerUsername,
  private val accountId: AccountId,
  private val accountCurrency: AccountCurrency,
) :
  DomainEvent(id, walletId, occurredOn)
