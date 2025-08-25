package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command

import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountCreated
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import java.util.UUID.randomUUID

class CreateAccountShould {

  @Test
  fun `create account`() {
    val id = randomUUID().toString()
    val walletId = randomUUID().toString()
    val accountCurrency = "EUR"

    val accountExists = mock<AccountExists> {
      on { with(AccountId(id)) } doReturn false
    }
    val accountRepository = mock<AccountRepository>()
    val eventBus = mock<EventBus>()
    val testSubject = CreateAccount(accountExists, accountRepository, eventBus)

    val command = CreateAccountCommand(
        accountId = id,
        accountCurrency,
        walletId,
    )
    testSubject.execute(command)

    argumentCaptor<Account> {
      verify(accountRepository).save(capture())
      val expected = Account.load(
          id,
          walletId,
          accountCurrency,
          deposits = mutableSetOf(),
          transfers = mutableSetOf(),
      )

      assertThat(firstValue).isEqualTo(expected)
    }

    argumentCaptor<Set<DomainEvent>> {
      verify(eventBus).publish(capture())
      assertThat(firstValue)
          .hasSize(1)
          .first()
          .isInstanceOf(AccountCreated::class.java)
          .satisfies(
              {
                val event = it as AccountCreated
                assertThat(event.aggregateId()).isEqualTo(id)
              },
          )
    }
  }

  @Test
  fun `not create account when already exists`() {
    val id = randomUUID().toString()
    val walletId = randomUUID().toString()
    val accountCurrency = "EUR"

    val accountExists = mock<AccountExists> {
      on { with(AccountId(id)) } doReturn true
    }
    val accountRepository = mock<AccountRepository>()
    val eventBus = mock<EventBus>()
    val testSubject = CreateAccount(accountExists, accountRepository, eventBus)

    val command = CreateAccountCommand(
        accountId = id,
        accountCurrency,
        walletId,
    )
    testSubject.execute(command)

    verify(accountRepository, never()).save(any<Account>())
    verify(eventBus, never()).publish(any())
  }
}
