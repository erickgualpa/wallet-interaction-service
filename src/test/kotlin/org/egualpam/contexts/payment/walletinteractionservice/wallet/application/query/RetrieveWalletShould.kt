package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.query

import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.exceptions.WalletNotExists
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

