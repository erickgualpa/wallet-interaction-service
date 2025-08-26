package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.query

import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountBalance
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountNotExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Transfer
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.UUID.randomUUID

class RetrieveAccountBalanceShould {

  @Test
  fun `retrieve account balance`() {
    val accountId = randomUUID().toString()

    val account = Account.load(
        id = accountId,
        walletId = randomUUID().toString(),
        currency = "EUR",
        balance = AccountBalance(100.0),
        deposits = mutableSetOf(
            Deposit.load(
                id = randomUUID().toString(),
                amount = 100.0,
            ),
        ),
        transfers = mutableSetOf(
            Transfer.load(
                id = randomUUID().toString(),
                sourceAccountId = accountId,
                destinationAccountId = randomUUID().toString(),
                amount = 25.5,
                isInbound = false,
            ),
        ),
    )

    val repository = mock<AccountRepository> {
      on {
        find(AccountId(accountId))
      } doReturn account
    }
    val testee = RetrieveAccountBalance(repository)

    val query = RetrieveAccountBalanceQuery(accountId)
    val result = testee.execute(query)

    val expected = AccountBalanceDto(
        balance = "74.5",
        currency = "EUR",
    )
    assertThat(result).usingRecursiveComparison().isEqualTo(expected)
  }

  @Test
  fun `throw domain exception when account not exists`() {
    val accountId = randomUUID().toString()

    val repository = mock<AccountRepository> {
      on {
        find(AccountId(accountId))
      } doReturn null
    }
    val testee = RetrieveAccountBalance(repository)

    val query = RetrieveAccountBalanceQuery(accountId)
    assertThrows<AccountNotExists> { testee.execute(query) }
  }
}
