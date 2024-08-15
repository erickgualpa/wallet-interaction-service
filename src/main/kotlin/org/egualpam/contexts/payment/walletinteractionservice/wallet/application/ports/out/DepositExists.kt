package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.DepositId

interface DepositExists {
  fun with(depositId: DepositId): Boolean
}
