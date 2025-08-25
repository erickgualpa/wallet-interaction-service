package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.out.accountexists

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.support.TransactionTemplate
import java.util.UUID.randomUUID
import kotlin.test.assertTrue

class AccountExistsMySQLAdapterIT(
  @Autowired private val accountRepository: AccountRepository,
  @Autowired private val transactionTemplate: TransactionTemplate,
  @Autowired private val jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractIntegrationTest() {
  @Test
  fun `return true when account exists`() {
    val accountId = randomUUID().toString()

    transactionTemplate.executeWithoutResult {
      val walletId = randomUUID().toString()

      val account = Account.load(
          id = accountId,
          currency = "EUR",
          walletId = walletId,
          deposits = mutableSetOf(),
          transfers = mutableSetOf(),
      )

      accountRepository.save(account)
    }

    val testSubject: AccountExists = AccountExistsMySQLAdapter(jdbcTemplate)
    val result = testSubject.with(AccountId(accountId))

    assertTrue(result)
  }
}
