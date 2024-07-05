package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEntity

class Owner(private val id: OwnerId) : DomainEntity {
  override fun getId() = id
}
