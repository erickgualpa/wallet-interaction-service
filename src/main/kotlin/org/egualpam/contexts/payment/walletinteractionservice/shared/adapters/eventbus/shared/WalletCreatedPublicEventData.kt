package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.shared

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletCreated

data class WalletCreatedPublicEventData(
  val ownerId: String,
  val ownerUsername: String,
  val accountId: String,
  val accountCurrency: String,
) : PublicEventData() {
  companion object {
    fun from(event: WalletCreated) = WalletCreatedPublicEventData(
        ownerId = event.ownerId(),
        ownerUsername = event.ownerUsername(),
        accountId = event.accountId(),
        accountCurrency = event.accountCurrency(),
    )
  }
}
