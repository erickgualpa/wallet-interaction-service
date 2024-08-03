package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletCreated
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import java.util.UUID.randomUUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CreateWalletShould {
  @Test
  fun `create wallet`() {
    val walletId = randomUUID().toString()
    val ownerId = randomUUID().toString()
    val ownerUsername = randomAlphabetic(10)
    val accountId = randomUUID().toString()
    val accountCurrency = "EUR"

    val walletExists = mock<WalletExists>()
    val saveWallet = mock<SaveWallet>()
    val createWalletCommand = CreateWalletCommand(
        walletId,
        ownerId,
        ownerUsername,
        accountId,
        accountCurrency,
    )

    CreateWallet(walletExists, saveWallet).execute(createWalletCommand)

    argumentCaptor<Wallet> {
      verify(saveWallet).save(capture())
      assertThat(firstValue).satisfies(
          {
            assertEquals(walletId, it.getId().value)
            assertEquals(ownerId, it.getOwnerId().value)
            assertEquals(ownerUsername, it.getOwnerUsername().value)
            assertEquals(accountId, it.getAccountId().value)
          },
      )
      assertThat(firstValue.pullDomainEvents()).hasSize(1).first().satisfies(
          {
            assertThat(it).isInstanceOf(WalletCreated::class.java)
            assertNotNull(it.id())
            assertNotNull(it.occurredOn())
          },
      )
    }
  }

  @Test
  fun `not create wallet when already exists`() {
    val walletId = randomUUID().toString()
    val ownerId = randomUUID().toString()
    val ownerUsername = randomAlphabetic(10)
    val accountId = randomUUID().toString()
    val accountCurrency = "EUR"

    val walletExists = mock<WalletExists> {
      on { with(WalletId(walletId)) } doReturn true
    }
    val saveWallet = mock<SaveWallet>()
    val createWalletCommand = CreateWalletCommand(
        walletId,
        ownerId,
        ownerUsername,
        accountId,
        accountCurrency,
    )

    CreateWallet(walletExists, saveWallet).execute(createWalletCommand)

    verify(saveWallet, never()).save(any())
  }

  @Test
  fun `throw domain exception when wallet id is invalid`() {
    val invalidWalletId = randomAlphanumeric(10)
    val ownerId = randomUUID().toString()
    val ownerUsername = randomAlphabetic(10)
    val accountId = randomUUID().toString()
    val accountCurrency = "EUR"

    val walletExists = mock<WalletExists>()
    val saveWallet = mock<SaveWallet>()
    val createWalletCommand = CreateWalletCommand(
        invalidWalletId,
        ownerId,
        ownerUsername,
        accountId,
        accountCurrency,
    )

    val exception = assertThrows<InvalidDomainEntityId> {
      CreateWallet(walletExists, saveWallet).execute(createWalletCommand)
    }

    assertThat(exception).hasMessage("The provided id [$invalidWalletId] is invalid")
  }

  @Test
  fun `throw domain exception when owner id is invalid`() {
    val walletId = randomUUID().toString()
    val invalidOwnerId = randomAlphanumeric(10)
    val ownerUsername = randomAlphabetic(10)
    val accountId = randomUUID().toString()
    val accountCurrency = "EUR"

    val walletExists = mock<WalletExists>()
    val saveWallet = mock<SaveWallet>()
    val createWalletCommand = CreateWalletCommand(
        walletId,
        invalidOwnerId,
        ownerUsername,
        accountId,
        accountCurrency,
    )

    val exception = assertThrows<InvalidDomainEntityId> {
      CreateWallet(walletExists, saveWallet).execute(createWalletCommand)
    }

    assertThat(exception).hasMessage("The provided id [$invalidOwnerId] is invalid")
  }

  @Test
  fun `throw domain exception when account id is invalid`() {
    val walletId = randomUUID().toString()
    val ownerId = randomUUID().toString()
    val ownerUsername = randomAlphabetic(10)
    val invalidAccountId = randomAlphabetic(10)
    val accountCurrency = "EUR"

    val walletExists = mock<WalletExists>()
    val saveWallet = mock<SaveWallet>()
    val createWalletCommand = CreateWalletCommand(
        walletId,
        ownerId,
        ownerUsername,
        invalidAccountId,
        accountCurrency,
    )

    val exception = assertThrows<InvalidDomainEntityId> {
      CreateWallet(walletExists, saveWallet).execute(createWalletCommand)
    }

    assertThat(exception).hasMessage("The provided id [$invalidAccountId] is invalid")
  }
}
