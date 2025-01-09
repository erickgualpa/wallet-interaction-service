package org.egualpam.contexts.payment.walletinteractionservice.journey

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphabetic
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.com.google.common.net.HttpHeaders.CONTENT_TYPE
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextDouble

class FullJourneyTest : AbstractIntegrationTest() {
  @Test
  fun `full journey`() {
    val walletId = randomUUID().toString()
    val currency = "EUR"

    val createWalletRequest = """
      {
        "wallet": {
          "id": "$walletId",
          "owner": {
            "id": "${randomUUID()}",
            "username": "${getRandomAlphabetic(10)}"
          },
          "account": {
            "id": "${randomUUID()}",
            "currency": "$currency"
          }
        }
      }
    """

    webTestClient.put()
        .uri("/v1/wallets")
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(createWalletRequest)
        .exchange()
        .expectStatus()
        .isNoContent

    webTestClient.get()
        .uri("/v1/wallets/{wallet-id}", walletId)
        .exchange()
        .expectStatus()
        .isOk

    val walletDepositRequest = """
      {
        "deposit": {
          "id": "${randomUUID()}",
          "amount": "${nextDouble(10.0, 10000.0)}",
          "currency": "$currency"
        }
      }
    """

    webTestClient.put()
        .uri("/v1/wallets/{wallet-id}/deposit", walletId)
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(walletDepositRequest)
        .exchange()
        .expectStatus()
        .isNoContent
  }
}
