package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId

interface AccountExists {
  fun with(id: AccountId): Boolean
}
