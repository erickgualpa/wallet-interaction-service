package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class TransferProcessed(
  id: DomainEventId,
  occurredOn: Instant,
  transferId: TransferId,
  sourceAccountId: AccountId,
  destinationAccountId: AccountId,
  amount: TransferAmount,
) :
  DomainEvent(id, sourceAccountId, occurredOn)
