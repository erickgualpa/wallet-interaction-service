package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command

import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountBalance
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountNotExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.DepositProcessed
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
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
    val depositAmount = 100.0
    val currency = "EUR"
    val accountId = randomUUID().toString()
    val walletId = randomUUID().toString()

    val existing = Account.load(
        accountId,
        walletId,
        currency,
        balance = AccountBalance(0.0),
        deposits = mutableSetOf(),
        transfers = mutableSetOf(),
    )

    val repository = mock<AccountRepository> {
      on { find(AccountId(accountId)) } doReturn existing
    }
    val eventBus = mock<EventBus>()

    val testSubject = DepositMoney(repository, eventBus)

    val command = DepositMoneyCommand(
        depositId,
        depositAmount,
        currency,
        accountId,
    )
    testSubject.execute(command)

    val expected = Account.load(
        accountId,
        walletId,
        currency,
        balance = AccountBalance(depositAmount),
        mutableSetOf(Deposit.load(depositId, depositAmount)),
        transfers = mutableSetOf(),
    )
    argumentCaptor<Account> {
      verify(repository).save(capture())
      assertThat(firstValue).usingRecursiveComparison().isEqualTo(expected)
    }

    argumentCaptor<Set<DomainEvent>> {
      verify(eventBus).publish(capture())
      assertThat(firstValue).hasSize(1).first().isInstanceOf(DepositProcessed::class.java)
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
    val eventBus = mock<EventBus>()

    val testSubject = DepositMoney(repository, eventBus)

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
    verify(eventBus, never()).publish(any())
  }
}
