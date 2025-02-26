package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import java.time.Instant

class AccountCreated(
  id: DomainEventId,
  accountId: AccountId,
  occurredOn: Instant
) :
  DomainEvent(
      id,
      accountId,
      occurredOn,
  )
