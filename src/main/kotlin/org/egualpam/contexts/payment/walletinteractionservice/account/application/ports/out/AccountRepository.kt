package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId

interface AccountRepository {
  fun find(id: AccountId): Account?
  fun save(account: Account)
}
