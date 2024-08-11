package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextInt

class RetrieveWalletFeature : AbstractIntegrationTest() {
  @Test
  fun `retrieve wallet`() {
    val walletPersistenceId = nextInt(100, 999)
    val walletEntityId = randomUUID().toString()
    walletTestRepository.createWallet(walletPersistenceId, walletEntityId)

    val ownerId = randomUUID().toString()
    val ownerUsername = randomAlphabetic(10)
    ownerTestRepository.createOwner(ownerId, ownerUsername, walletPersistenceId, walletEntityId)

    val accountId = randomUUID().toString()
    accountTestRepository.createAccount(
        accountId,
        currency = "EUR",
        walletPersistenceId,
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
    val invalidWalletId = randomAlphanumeric(10)

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
