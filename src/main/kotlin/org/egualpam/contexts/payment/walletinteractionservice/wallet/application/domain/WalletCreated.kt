package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class WalletCreated(private val id: DomainEventId, wallet: Wallet) : DomainEvent(wallet) {
  // TODO: Set this as a constructor parameter
  private val occurredOn = Instant.now()

  override fun id() = id
  override fun occurredOn(): Instant = occurredOn
}
