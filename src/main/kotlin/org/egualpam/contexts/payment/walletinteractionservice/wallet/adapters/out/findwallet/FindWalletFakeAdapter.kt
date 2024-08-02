package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.WalletDto

class FindWalletFakeAdapter : FindWallet {
  override fun find(id: WalletId) = WalletDto(
      id.value,
      WalletDto.OwnerDto("fake-owner-id"),
      WalletDto.AccountDto("fake-account-id"),
  )
}
