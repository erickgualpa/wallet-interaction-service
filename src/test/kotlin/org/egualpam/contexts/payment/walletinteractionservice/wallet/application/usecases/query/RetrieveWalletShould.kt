package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletNotExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.UUID.randomUUID

class RetrieveWalletShould {
  @Test
  fun `retrieve wallet`() {
    val walletId = randomUUID().toString()
    val ownerId = randomUUID().toString()
    val accountId = randomUUID().toString()

    val findWalletPort = mock<FindWalletPort> {
      on {
        find(WalletId(walletId))
      } doReturn WalletDto(
          walletId,
          WalletDto.OwnerDto(ownerId),
          WalletDto.AccountDto(accountId),
      )
    }
    val retrieveWalletQuery = RetrieveWalletQuery(walletId)

    val result = RetrieveWallet(findWalletPort).execute(retrieveWalletQuery)

    assertThat(result).usingRecursiveComparison().isEqualTo(
        WalletDto(
            walletId,
            WalletDto.OwnerDto(ownerId),
            WalletDto.AccountDto(accountId),
        ),
    )
  }

  @Test
  fun `throw domain exception when wallet id is not valid`() {
    val invalidWalletId = randomAlphabetic(10)

    val findWalletPort = mock<FindWalletPort>()
    val retrieveWalletQuery = RetrieveWalletQuery(invalidWalletId)
    val testSubject = RetrieveWallet(findWalletPort)

    val exception = assertThrows<InvalidDomainEntityId> { testSubject.execute(retrieveWalletQuery) }

    assertThat(exception).hasMessage("The provided id [$invalidWalletId] is invalid")
  }

  @Test
  fun `throw domain exception when wallet not exists`() {
    val walletId = randomUUID().toString()

    val findWalletPort = mock<FindWalletPort> {
      on {
        find(WalletId(walletId))
      } doReturn null
    }
    val retrieveWalletQuery = RetrieveWalletQuery(walletId)
    val testSubject = RetrieveWallet(findWalletPort)

    val exception = assertThrows<WalletNotExists> { testSubject.execute(retrieveWalletQuery) }

    assertThat(exception).hasMessage("Wallet with id [$walletId] not exists")
  }
}

