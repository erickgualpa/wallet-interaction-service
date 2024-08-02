package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class WalletCreated(val wallet: Wallet) : DomainEvent() {
  private val id = DomainEventId.generate()
  private val occurredOn = Instant.now()

  override fun id() = id
  override fun occurredOn(): Instant = occurredOn
}
