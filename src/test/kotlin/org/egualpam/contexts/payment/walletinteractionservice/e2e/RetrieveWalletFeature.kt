package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphabetic
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphanumeric
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID

class RetrieveWalletFeature : AbstractIntegrationTest() {
  @Test
  fun `retrieve wallet`() {
    val walletEntityId = randomUUID().toString()
    walletTestRepository.createWallet(walletEntityId)

    val ownerId = randomUUID().toString()
    val ownerUsername = getRandomAlphabetic(10)
    ownerTestRepository.createOwner(ownerId, ownerUsername, walletEntityId)

    val accountId = randomUUID().toString()
    accountTestRepository.createAccount(
        accountId,
        currency = "EUR",
        walletEntityId,
    )

    webTestClient.get()
        .uri("/v1/wallets/{wallet-id}", walletEntityId)
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType("application/json")
        .expectBody()
        .json(
            """
              {
                "wallet": {
                  "id": "$walletEntityId",
                  "owner": {
                    "id": "$ownerId"
                  },
                  "accounts": [
                    {
                      "id": "$accountId"
                    }
                  ]
                }
              }
            """,
        )
  }

  @Test
  fun `get 404 NOT FOUND when wallet id is not valid`() {
    val invalidWalletId = getRandomAlphanumeric(10)

    webTestClient.get()
        .uri("/v1/wallets/{wallet-id}", invalidWalletId)
        .exchange()
        .expectStatus()
        .isNotFound
  }

  @Test
  fun `get 404 NOT FOUND when wallet matching wallet id not exists`() {
    val randomWalletId = randomUUID().toString()

    webTestClient.get()
        .uri("/v1/wallets/{wallet-id}", randomWalletId)
        .exchange()
        .expectStatus()
        .isNotFound
  }
}
