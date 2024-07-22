package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEntity

class Owner(
  private val id: OwnerId,
  private val username: OwnerUsername
) : DomainEntity() {
  override fun getId() = id

  fun getUsername() = this.username
}
