package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Owner
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.OwnerId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort

class FindWalletFakeAdapter : FindWalletPort {
  override fun find(id: WalletId) = Wallet(
      id,
      Owner(OwnerId("fake-owner-id")),
      Account(AccountId("fake-account-id")),
  )
}
