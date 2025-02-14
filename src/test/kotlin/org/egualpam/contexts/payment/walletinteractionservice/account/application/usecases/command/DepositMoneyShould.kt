package org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command

import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextDouble

class DepositMoneyShould {

  @Test
  fun `deposit money`() {
    val accountId = randomUUID().toString()
    val currency = "EUR"

    val existing = Account.load(accountId)

    val repository = mock<AccountRepository> {
      on { find(AccountId(accountId)) } doReturn existing
    }

    val testSubject = DepositMoney(repository)

    val command = DepositMoneyCommand(
        id = randomUUID().toString(),
        amount = nextDouble(),
        currency,
        accountId,
    )
    testSubject.execute(command)


    val expected = Account.load(accountId)
    argumentCaptor<Account> {
      verify(repository).save(capture())
      assertThat(firstValue).usingRecursiveComparison().isEqualTo(expected)
    }
  }
}
