package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.query.WalletDto
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId

class FindWalletFakeAdapter : FindWalletPort {
  override fun find(id: WalletId) = WalletDto(
      id.value,
      WalletDto.OwnerDto("fake-owner-id"),
      WalletDto.AccountDto("fake-account-id"),
  )
}
