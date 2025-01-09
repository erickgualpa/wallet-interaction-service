package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.TemporalUnitWithinOffset
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphabetic
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.com.google.common.net.HttpHeaders.CONTENT_TYPE
import java.time.Instant
import java.time.temporal.ChronoUnit.SECONDS
import java.util.UUID.randomUUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CreateWalletFeature : AbstractIntegrationTest() {
  @Test
  fun `create wallet`() {
    val walletId = randomUUID().toString()
    val ownerId = randomUUID().toString()
    val accountId = randomUUID().toString()

    val username = getRandomAlphabetic(10)
    val currency = "EUR"

    val request = """
      {
        "wallet": {
          "id": "$walletId",
          "owner": {
            "id": "$ownerId",
            "username": "$username"
          },
          "account": {
            "id": "$accountId",
            "currency": "$currency"
          }
        }
      }
    """

    webTestClient.put()
        .uri("/v1/wallets")
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isNoContent

    val walletResult = walletTestRepository.findWallet(walletId)
    assertThat(walletResult).satisfies(
        {
          assertNotNull(it)
          assertEquals(walletId, it.id)
          assertThat(it.createdAt).isCloseTo(
              Instant.now(),
              TemporalUnitWithinOffset(1, SECONDS),
          )
        },
    )

    val ownerResult = ownerTestRepository.findOwner(ownerId)
    assertThat(ownerResult).satisfies(
        {
          assertNotNull(it)
          assertEquals(ownerId, it.id)
          assertEquals(username, it.username)
          assertThat(it.createdAt).isCloseTo(
              Instant.now(),
              TemporalUnitWithinOffset(1, SECONDS),
          )
        },
    )

    val accountResult = accountTestRepository.findAccount(accountId)
    assertThat(accountResult).satisfies(
        {
          assertNotNull(it)
          assertEquals(accountId, it.id)
          assertEquals(currency, it.currency)
          assertThat(it.createdAt).isCloseTo(
              Instant.now(),
              TemporalUnitWithinOffset(1, SECONDS),
          )
        },
    )
  }
}
