package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomUtils.nextDouble
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.com.google.common.net.HttpHeaders.CONTENT_TYPE
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextInt

class DepositMoneyFeature : AbstractIntegrationTest() {
  @Test
  fun `deposit money`() {
    val walletId = randomUUID().toString()
    createWallet(walletId)

    val depositId = randomUUID().toString()
    val amount = nextDouble(10.0, 10000.0)
    val currency = "EUR"

    val request = """
      {
        "deposit": {
          "id": "$depositId",
          "amount": "$amount",
          "currency": "$currency"
        }
      }
    """

    webTestClient.put()
        .uri("/v1/wallets/{wallet-id}/deposit", walletId)
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isNoContent

    // TODO: Verify deposit has been persisted in db
  }

  private fun createWallet(walletId: String) {
    val walletPersistenceId = nextInt(100, 999)
    walletTestRepository.createWallet(walletPersistenceId, walletId)

    val ownerId = randomUUID().toString()
    val ownerUsername = randomAlphabetic(10)
    ownerTestRepository.createOwner(ownerId, ownerUsername, walletPersistenceId, walletId)

    val accountId = randomUUID().toString()
    accountTestRepository.createAccount(
        accountEntityId = accountId,
        currency = "EUR",
        walletPersistenceId = walletPersistenceId,
        walletEntityId = walletId,
    )
  }
}
