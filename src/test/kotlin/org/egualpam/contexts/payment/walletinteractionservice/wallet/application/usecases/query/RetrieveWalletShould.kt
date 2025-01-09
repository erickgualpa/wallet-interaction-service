package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query

import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidAggregateId
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphabetic
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletNotExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletSearchRepository
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

    val walletSearchRepository = mock<WalletSearchRepository> {
      on {
        search(WalletId(walletId))
      } doReturn WalletDto(
          walletId,
          WalletDto.OwnerDto(ownerId),
          setOf(WalletDto.AccountDto(accountId)),
      )
    }
    val retrieveWalletQuery = RetrieveWalletQuery(walletId)

    val result = RetrieveWallet(walletSearchRepository).execute(retrieveWalletQuery)

    assertThat(result).usingRecursiveComparison().isEqualTo(
        WalletDto(
            walletId,
            WalletDto.OwnerDto(ownerId),
            setOf(WalletDto.AccountDto(accountId)),
        ),
    )
  }

  @Test
  fun `throw domain exception when wallet id is not valid`() {
    val invalidWalletId = getRandomAlphabetic(10)

    val walletSearchRepository = mock<WalletSearchRepository>()
    val retrieveWalletQuery = RetrieveWalletQuery(invalidWalletId)
    val testSubject = RetrieveWallet(walletSearchRepository)

    val exception = assertThrows<InvalidAggregateId> { testSubject.execute(retrieveWalletQuery) }

    assertThat(exception).hasMessage("The provided id [$invalidWalletId] is invalid")
  }

  @Test
  fun `throw domain exception when wallet not exists`() {
    val walletId = randomUUID().toString()

    val walletSearchRepository = mock<WalletSearchRepository> {
      on {
        search(WalletId(walletId))
      } doReturn null
    }
    val retrieveWalletQuery = RetrieveWalletQuery(walletId)
    val testSubject = RetrieveWallet(walletSearchRepository)

    val exception = assertThrows<WalletNotExists> { testSubject.execute(retrieveWalletQuery) }

    assertThat(exception).hasMessage("Wallet with id [$walletId] not exists")
  }
}

