package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command

import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountNotExists
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Transfer
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.UUID.randomUUID

class TransferMoneyShould {

  companion object {
    private const val CURRENCY = "EUR"
  }

  @Test
  fun `transfer money`() {
    val transferId = randomUUID().toString()
    val sourceAccountId = randomUUID().toString()
    val destinationAccountId = randomUUID().toString()
    val amount = 100.0

    val sourceAccount = Account.load(
        id = sourceAccountId,
        currency = CURRENCY,
        walletId = randomUUID().toString(),
        deposits = mutableSetOf(
            Deposit.load(
                id = randomUUID().toString(),
                amount = 200.0,
            ),
        ),
    )
    val destinationAccount = Account.load(
        id = destinationAccountId,
        currency = CURRENCY,
        walletId = randomUUID().toString(),
        deposits = mutableSetOf(), // No deposits yet
    )
    val accountRepository = mock<AccountRepository> {
      on { find(AccountId(sourceAccountId)) } doReturn sourceAccount
      on { find(AccountId(destinationAccountId)) } doReturn destinationAccount
    }
    val testee = TransferMoney(accountRepository)

    val command = TransferMoneyCommand(
        transferId,
        sourceAccountId,
        destinationAccountId,
        amount,
    )
    testee.execute(command)

    argumentCaptor<Account> {
      verify(accountRepository, times(2)).save(capture())

      // Validate source account state
      val expectedOutboundTransfer = Transfer.load(
          id = transferId,
          sourceAccountId = sourceAccountId,
          destinationAccountId = destinationAccountId,
          amount = amount,
          isInbound = false,
      )
      assertThat(firstValue.transfers())
          .hasSize(1)
          .first()
          .usingRecursiveComparison()
          .isEqualTo(expectedOutboundTransfer)

      assertThat(firstValue.balance().value).isEqualTo(100.00)

      // Validate destination account state
      val expectedInboundTransfer = Transfer.load(
          id = transferId,
          sourceAccountId = sourceAccountId,
          destinationAccountId = destinationAccountId,
          amount = amount,
          isInbound = true,
      )
      assertThat(secondValue.transfers())
          .hasSize(1)
          .first()
          .usingRecursiveComparison()
          .isEqualTo(expectedInboundTransfer)

      assertThat(secondValue.balance().value).isEqualTo(100.00)
    }
  }

  @Test
  fun `throw domain exception if source account not exists`() {
    val transferId = randomUUID().toString()
    val sourceAccountId = randomUUID().toString()
    val destinationAccountId = randomUUID().toString()
    val amount = 100.0

    val destinationAccount = Account.load(
        id = destinationAccountId,
        currency = CURRENCY,
        walletId = randomUUID().toString(),
        deposits = mutableSetOf(), // No deposits yet
    )
    val accountRepository = mock<AccountRepository> {
      on { find(AccountId(sourceAccountId)) } doReturn null
      on { find(AccountId(destinationAccountId)) } doReturn destinationAccount
    }
    val testee = TransferMoney(accountRepository)

    val command = TransferMoneyCommand(
        transferId,
        sourceAccountId,
        destinationAccountId,
        amount,
    )
    assertThrows<AccountNotExists> {
      testee.execute(command)
    }

    verify(accountRepository, never()).save(destinationAccount)
  }

  @Test
  fun `throw domain exception if destination account not exists`() {
    val transferId = randomUUID().toString()
    val sourceAccountId = randomUUID().toString()
    val destinationAccountId = randomUUID().toString()
    val amount = 100.0

    val sourceAccount = Account.load(
        id = sourceAccountId,
        currency = CURRENCY,
        walletId = randomUUID().toString(),
        deposits = mutableSetOf(
            Deposit.load(
                id = randomUUID().toString(),
                amount = 200.0,
            ),
        ),
    )
    val accountRepository = mock<AccountRepository> {
      on { find(AccountId(sourceAccountId)) } doReturn sourceAccount
      on { find(AccountId(destinationAccountId)) } doReturn null
    }
    val testee = TransferMoney(accountRepository)

    val command = TransferMoneyCommand(
        transferId,
        sourceAccountId,
        destinationAccountId,
        amount,
    )
    assertThrows<AccountNotExists> {
      testee.execute(command)
    }

    verify(accountRepository, never()).save(sourceAccount)
  }
}
