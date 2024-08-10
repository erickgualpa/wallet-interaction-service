package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.WalletDto

interface WalletSearchRepository {
  fun search(id: WalletId): WalletDto?
}
