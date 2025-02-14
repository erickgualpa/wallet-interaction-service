package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.AccountCurrency
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class DepositProcessed(
  id: DomainEventId,
  walletId: WalletId,
  occurredOn: Instant,
  private val depositId: DepositId,
  private val amount: DepositAmount,
  private val accountCurrency: AccountCurrency
) :
  DomainEvent(id, walletId, occurredOn)
