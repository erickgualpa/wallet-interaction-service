package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.DepositId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.DepositProcessed
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Owner
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletNotExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.DepositExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository
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
import kotlin.test.assertEquals

class DepositMoneyShould {
  @Test
  fun `deposit money`() {
    val walletId = randomUUID().toString()
    val depositId = randomUUID().toString()
    val depositAmount = nextDouble(10.0, 10000.0)
    val accountCurrency = "EUR"

    val depositExists = mock<DepositExists>()
    val walletRepository = mock<WalletRepository> {
      on {
        find(WalletId(walletId))
      } doReturn Wallet(
          id = WalletId(walletId),
          owner = Owner.create(
              id = randomUUID().toString(),
              username = randomAlphabetic(5),
          ),
          accounts = mutableSetOf(
              Account.create(
                  id = randomUUID().toString(),
                  currency = accountCurrency,
              ),
          ),
      )
    }

    val depositMoneyCommand = DepositMoneyCommand(
        walletId = walletId,
        depositId = depositId,
        depositAmount = depositAmount,
        depositCurrency = accountCurrency,
    )

    DepositMoney(depositExists, walletRepository).execute(depositMoneyCommand)

    argumentCaptor<Wallet> {
      verify(walletRepository).save(capture())

      val accounts = firstValue.accounts()
      assertThat(accounts).hasSize(1)

      val deposits = accounts.first().deposits()
      assertThat(deposits).hasSize(1)
      assertThat(deposits).first().satisfies(
          {
            assertEquals(depositId, it.getId().value)
            assertEquals(depositAmount, it.amount().value)
          },
      )

      assertThat(firstValue.pullDomainEvents())
          .hasSize(1)
          .first()
          .isInstanceOf(DepositProcessed::class.java)
    }
  }

  @Test
  fun `not deposit money when deposit is already processed`() {
    val depositId = randomUUID().toString()

    val depositExists = mock<DepositExists> {
      on { with(DepositId(depositId)) } doReturn true
    }
    val walletRepository = mock<WalletRepository>()

    val depositMoneyCommand = DepositMoneyCommand(
        walletId = randomUUID().toString(),
        depositId = depositId,
        depositAmount = nextDouble(10.0, 10000.0),
        depositCurrency = "EUR",
    )

    DepositMoney(depositExists, walletRepository).execute(depositMoneyCommand)

    verify(walletRepository, never()).find(any<WalletId>())
    verify(walletRepository, never()).save(any<Wallet>())
  }

  @Test
  fun `throw domain exception when wallet not exists`() {
    val walletId = randomUUID().toString()

    val depositExists = mock<DepositExists>()
    val walletRepository = mock<WalletRepository> {
      on {
        find(WalletId(walletId))
      } doReturn null
    }

    val depositMoneyCommand = DepositMoneyCommand(
        walletId = walletId,
        depositId = randomUUID().toString(),
        depositAmount = nextDouble(10.0, 10000.0),
        depositCurrency = "EUR",
    )

    val exception = assertThrows<WalletNotExists> {
      DepositMoney(depositExists, walletRepository).execute(depositMoneyCommand)
    }
    assertThat(exception).hasMessage("Wallet with id [$walletId] not exists")
  }
}
