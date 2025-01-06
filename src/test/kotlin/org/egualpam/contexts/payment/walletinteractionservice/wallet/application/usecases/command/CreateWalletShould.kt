package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventIdSupplier
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidAggregateId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.OwnerUsername
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletCreated
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.AccountCurrencyIsNotSupported
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.OwnerUsernameAlreadyExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
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
    val domainEventId = DomainEventId.generate()

    val domainEventIdSupplier = mock<DomainEventIdSupplier> {
      on {
        get()
      } doReturn domainEventId
    }
    val walletExists = mock<WalletExists>()
    val walletRepository = mock<WalletRepository>()
    val eventBus = mock<EventBus>()
    val testSubject = CreateWallet(walletExists, walletRepository, eventBus, domainEventIdSupplier)

    val createWalletCommand = CreateWalletCommand(
        walletId,
        ownerId,
        ownerUsername,
        accountId,
        accountCurrency,
    )
    testSubject.execute(createWalletCommand)

    argumentCaptor<Wallet> {
      verify(walletRepository).save(capture())
      assertThat(firstValue).satisfies(
          {
            assertEquals(walletId, it.getId().value)
            assertEquals(ownerId, it.getOwnerId().value)
            assertEquals(ownerUsername, it.getOwnerUsername().value)
            assertThat(it.accounts()).hasSize(1)
          },
      )
      assertThat(firstValue.accounts()).first().satisfies(
          {
            assertEquals(accountId, it.getId().value)
            assertEquals(accountCurrency, it.getCurrency().value)
          },
      )
    }

    argumentCaptor<Set<DomainEvent>> {
      verify(eventBus).publish(capture())
      assertThat(firstValue).hasSize(1)
      assertThat(firstValue).first().satisfies(
          {
            assertThat(it).isInstanceOf(WalletCreated::class.java)
            assertThat(it.id()).isEqualTo(domainEventId)
            assertNotNull(it.occurredOn())
          },
      )
    }
  }

  @Test
  fun `not create wallet when already exists`() {
    val existingWalletId = randomUUID().toString()

    val domainEventIdSupplier = mock<DomainEventIdSupplier>()
    val walletExists = mock<WalletExists> {
      on { with(WalletId(existingWalletId)) } doReturn true
    }
    val walletRepository = mock<WalletRepository>()
    val eventBus = mock<EventBus>()
    val testSubject = CreateWallet(walletExists, walletRepository, eventBus, domainEventIdSupplier)

    val createWalletCommand = CreateWalletCommand(
        walletId = existingWalletId,
        ownerId = randomUUID().toString(),
        ownerUsername = randomAlphabetic(10),
        accountId = randomUUID().toString(),
        accountCurrency = "EUR",
    )
    testSubject.execute(createWalletCommand)

    verify(walletRepository, never()).save(any<Wallet>())
    verify(eventBus, never()).publish(any<Set<DomainEvent>>())
  }

  @Test
  fun `throw domain exception when wallet id is invalid`() {
    val invalidWalletId = randomAlphanumeric(10)

    val domainEventIdSupplier = mock<DomainEventIdSupplier>()
    val walletExists = mock<WalletExists>()
    val walletRepository = mock<WalletRepository>()
    val eventBus = mock<EventBus>()
    val testSubject = CreateWallet(walletExists, walletRepository, eventBus, domainEventIdSupplier)

    val createWalletCommand = CreateWalletCommand(
        walletId = invalidWalletId,
        ownerId = randomUUID().toString(),
        ownerUsername = randomAlphabetic(10),
        accountId = randomUUID().toString(),
        accountCurrency = "EUR",
    )
    val exception = assertThrows<InvalidAggregateId> {
      testSubject.execute(createWalletCommand)
    }

    assertThat(exception).hasMessage("The provided id [$invalidWalletId] is invalid")
  }

  @Test
  fun `throw domain exception when owner id is invalid`() {
    val invalidOwnerId = randomAlphanumeric(10)

    val domainEventIdSupplier = mock<DomainEventIdSupplier> {
      on {
        get()
      } doReturn DomainEventId.generate()
    }
    val walletExists = mock<WalletExists>()
    val walletRepository = mock<WalletRepository>()
    val eventBus = mock<EventBus>()
    val testSubject = CreateWallet(walletExists, walletRepository, eventBus, domainEventIdSupplier)

    val createWalletCommand = CreateWalletCommand(
        walletId = randomUUID().toString(),
        ownerId = invalidOwnerId,
        ownerUsername = randomAlphabetic(10),
        accountId = randomUUID().toString(),
        accountCurrency = "EUR",
    )
    val exception = assertThrows<InvalidDomainEntityId> {
      testSubject.execute(createWalletCommand)
    }

    assertThat(exception).hasMessage("The provided id [$invalidOwnerId] is invalid")
  }

  @Test
  fun `throw domain exception when account id is invalid`() {
    val invalidAccountId = randomAlphabetic(10)

    val domainEventIdSupplier = mock<DomainEventIdSupplier> {
      on {
        get()
      } doReturn DomainEventId.generate()
    }
    val walletExists = mock<WalletExists>()
    val walletRepository = mock<WalletRepository>()
    val eventBus = mock<EventBus>()
    val testSubject = CreateWallet(walletExists, walletRepository, eventBus, domainEventIdSupplier)

    val createWalletCommand = CreateWalletCommand(
        walletId = randomUUID().toString(),
        ownerId = randomUUID().toString(),
        ownerUsername = randomAlphabetic(10),
        accountId = invalidAccountId,
        accountCurrency = "EUR",
    )
    val exception = assertThrows<InvalidDomainEntityId> {
      testSubject.execute(createWalletCommand)
    }

    assertThat(exception).hasMessage("The provided id [$invalidAccountId] is invalid")
  }

  @Test
  fun `throw domain exception when owner username already exists`() {
    val existingOwnerUsername = randomAlphabetic(10)

    val domainEventIdSupplier = mock<DomainEventIdSupplier> {
      on {
        get()
      } doReturn DomainEventId.generate()
    }
    val walletExists = mock<WalletExists> {
      on { with(OwnerUsername(existingOwnerUsername)) } doReturn true
    }
    val walletRepository = mock<WalletRepository>()
    val eventBus = mock<EventBus>()
    val testSubject = CreateWallet(walletExists, walletRepository, eventBus, domainEventIdSupplier)

    val createWalletCommand = CreateWalletCommand(
        walletId = randomUUID().toString(),
        ownerId = randomUUID().toString(),
        ownerUsername = existingOwnerUsername,
        accountId = randomUUID().toString(),
        accountCurrency = "EUR",
    )
    val exception = assertThrows<OwnerUsernameAlreadyExists> {
      testSubject.execute(createWalletCommand)
    }

    assertThat(exception).hasMessage("Wallet owner with username [${existingOwnerUsername}] already exists")
  }

  @Test
  fun `throw domain exception when account currency is not supported`() {
    val unsupportedCurrency = "USD"

    val domainEventIdSupplier = mock<DomainEventIdSupplier> {
      on {
        get()
      } doReturn DomainEventId.generate()
    }
    val walletExists = mock<WalletExists>()
    val walletRepository = mock<WalletRepository>()
    val eventBus = mock<EventBus>()
    val testSubject = CreateWallet(walletExists, walletRepository, eventBus, domainEventIdSupplier)

    val createWalletCommand = CreateWalletCommand(
        walletId = randomUUID().toString(),
        ownerId = randomUUID().toString(),
        ownerUsername = randomAlphabetic(10),
        accountId = randomUUID().toString(),
        accountCurrency = unsupportedCurrency,
    )
    val exception = assertThrows<AccountCurrencyIsNotSupported> {
      testSubject.execute(createWalletCommand)
    }

    assertThat(exception).hasMessage("The provided currency [$unsupportedCurrency] is not supported")
  }
}
