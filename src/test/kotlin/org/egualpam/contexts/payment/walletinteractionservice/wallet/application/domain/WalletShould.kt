package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID

class WalletShould {
  @Test
  fun `define its identity by aggregate id`() {
    val walletId = randomUUID().toString()

    val wallet = Wallet(
        id = walletId,
        ownerId = randomUUID().toString(),
        ownerUsername = randomAlphabetic(5),
        accountId = randomUUID().toString(),
        accountCurrency = "EUR",
    )

    val walletInDifferentState = Wallet(
        id = walletId,
        ownerId = randomUUID().toString(),
        ownerUsername = randomAlphabetic(5),
        accountId = randomUUID().toString(),
        accountCurrency = "EUR",
    )

    assertTrue(wallet == walletInDifferentState)
  }
}
