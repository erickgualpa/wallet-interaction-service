package org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command

import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountNotExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextDouble

class DepositMoneyShould {

  @Test
  fun `deposit money`() {
    val depositId = randomUUID().toString()
    val depositAmount = nextDouble()
    val currency = "EUR"
    val accountId = randomUUID().toString()

    val existing = Account.load(
        accountId,
        currency,
        deposits = mutableSetOf(),
    )

    val repository = mock<AccountRepository> {
      on { find(AccountId(accountId)) } doReturn existing
    }

    val testSubject = DepositMoney(repository)

    val command = DepositMoneyCommand(
        depositId,
        depositAmount,
        currency,
        accountId,
    )
    testSubject.execute(command)


    val expected = Account.load(
        accountId,
        currency,
        mutableSetOf(Deposit.load(depositId, depositAmount)),
    )
    argumentCaptor<Account> {
      verify(repository).save(capture())
      assertThat(firstValue).usingRecursiveComparison().isEqualTo(expected)
    }
  }

  @Test
  fun `throw domain exception when account not exists`() {
    val depositId = randomUUID().toString()
    val depositAmount = nextDouble()
    val currency = "EUR"
    val accountId = randomUUID().toString()

    val repository = mock<AccountRepository> {
      on { find(AccountId(accountId)) } doReturn null
    }

    val testSubject = DepositMoney(repository)

    val command = DepositMoneyCommand(
        depositId,
        depositAmount,
        currency,
        accountId,
    )
    val exception = assertThrows<AccountNotExists> {
      testSubject.execute(command)
    }

    assertThat(exception).hasMessage("Account with id [$accountId] not exists")

    verify(repository, never()).save(any<Account>())
  }
}
