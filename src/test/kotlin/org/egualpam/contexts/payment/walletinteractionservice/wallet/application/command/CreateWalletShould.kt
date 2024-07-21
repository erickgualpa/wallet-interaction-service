package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.command

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.shared.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Wallet
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

class CreateWalletShould {
  @Test
  fun `create wallet`() {
    val walletId = randomUUID().toString()
    val ownerId = randomUUID().toString()
    val ownerUsername = randomAlphabetic(10)
    val accountId = randomUUID().toString()
    val accountCurrency = "EUR"

    val saveWalletPort = mock<SaveWalletPort>()
    val createWalletCommand = CreateWalletCommand(
        walletId,
        ownerId,
        ownerUsername,
        accountId,
        accountCurrency,
    )

    CreateWallet(saveWalletPort).execute(createWalletCommand)

    argumentCaptor<Wallet> {
      verify(saveWalletPort).save(capture())
      assertThat(firstValue).satisfies(
          {
            assertEquals(walletId, it.getId().value)
            assertEquals(ownerId, it.getOwnerId().value)
            assertEquals(ownerUsername, it.getOwnerUsername().value)
            assertEquals(accountId, it.getAccountId().value)
          },
      )
    }
  }

  @Test
  fun `throw domain exception when wallet id is invalid`() {
    val invalidWalletId = randomAlphanumeric(10)
    val ownerId = randomUUID().toString()
    val ownerUsername = randomAlphabetic(10)
    val accountId = randomUUID().toString()
    val accountCurrency = "EUR"

    val saveWalletPort = mock<SaveWalletPort>()
    val createWalletCommand = CreateWalletCommand(
        invalidWalletId,
        ownerId,
        ownerUsername,
        accountId,
        accountCurrency,
    )

    val exception = assertThrows<InvalidDomainEntityId> {
      CreateWallet(saveWalletPort).execute(createWalletCommand)
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

    val saveWalletPort = mock<SaveWalletPort>()
    val createWalletCommand = CreateWalletCommand(
        walletId,
        invalidOwnerId,
        ownerUsername,
        accountId,
        accountCurrency,
    )

    val exception = assertThrows<InvalidDomainEntityId> {
      CreateWallet(saveWalletPort).execute(createWalletCommand)
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

    val saveWalletPort = mock<SaveWalletPort>()
    val createWalletCommand = CreateWalletCommand(
        walletId,
        ownerId,
        ownerUsername,
        invalidAccountId,
        accountCurrency,
    )

    val exception = assertThrows<InvalidDomainEntityId> {
      CreateWallet(saveWalletPort).execute(createWalletCommand)
    }

    assertThat(exception).hasMessage("The provided id [$invalidAccountId] is invalid")
  }
}
