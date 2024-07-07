package org.egualpam.contexts.payment.walletinteractionservice.wallet.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.domain.DomainEntity

class Owner(private val id: OwnerId) : DomainEntity {
  override fun getId() = id
}
