package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Owner
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.OwnerId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId

class FindWalletFakeAdapter : FindWalletPort {
  override fun find(id: WalletId) = Wallet(
      id,
      Owner(OwnerId("fake-owner-id")),
      Account(AccountId("fake-account-id")),
  )
}
