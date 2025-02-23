package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.shared

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletCreated

data class WalletCreatedPublicEventData(
  val walletId: String,
  val ownerId: String,
  val ownerUsername: String,
  val accountId: String,
  val accountCurrency: String,
) : PublicEventData() {
  companion object {
    fun from(event: WalletCreated) = WalletCreatedPublicEventData(
        event.aggregateId(),
        // TODO: Map missing fields
        ownerId = "missing",
        ownerUsername = "missing",
        accountId = "missing",
        accountCurrency = "missing",
    )
  }
}
