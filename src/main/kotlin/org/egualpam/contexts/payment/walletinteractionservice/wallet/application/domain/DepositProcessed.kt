package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class DepositProcessed(private val id: DomainEventId, wallet: Wallet) : DomainEvent(wallet) {
  private val occurredOn = Instant.now()

  override fun id() = this.id
  override fun occurredOn(): Instant = this.occurredOn
}
