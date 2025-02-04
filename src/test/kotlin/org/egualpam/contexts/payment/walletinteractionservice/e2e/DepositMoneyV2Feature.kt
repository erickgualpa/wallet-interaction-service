package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphabetic
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.com.google.common.net.HttpHeaders.CONTENT_TYPE
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextDouble
import kotlin.test.assertNotNull

class DepositMoneyV2Feature : AbstractIntegrationTest() {
  @Test
  fun `deposit money`() {
    val walletId = randomUUID().toString()
    createWallet(walletId)

    val depositId = randomUUID().toString()
    val amount = nextDouble(10.0, 10000.0)
    val currency = "EUR"

    val request = """
      {
        "id": "$depositId",
        "amount": "$amount",
        "currency": "$currency",
        "walletId": "$walletId"
      }
    """

    webTestClient.put()
        .uri("/v1/deposits")
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isNoContent

    // TODO: Enable once new endpoint is available
    /*val depositResult = depositTestRepository.findDeposit(depositId)
    assertThat(depositResult).satisfies(
        {
          assertNotNull(it)
          assertEquals(depositId, it.id)
          assertThat(it.createdAt).isCloseTo(
              Instant.now(),
              TemporalUnitWithinOffset(1, SECONDS),
          )
        },
    )*/
  }

  // TODO: Update this test to perform request against new API
  @Disabled
  @Test
  fun `request multiple deposits`() {
    val walletId = randomUUID().toString()
    createWallet(walletId)

    val amount = nextDouble(10.0, 10000.0)
    val currency = "EUR"

    // First deposit
    val firstDepositId = randomUUID().toString()
    val firstDepositRequest = """
      {
        "deposit": {
          "id": "$firstDepositId",
          "amount": "$amount",
          "currency": "$currency"
        }
      }
    """

    webTestClient.put()
        .uri("/v1/wallets/{wallet-id}/deposit", walletId)
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(firstDepositRequest)
        .exchange()
        .expectStatus()
        .isNoContent

    // Next deposit
    val nextDepositId = randomUUID().toString()
    val nextDepositRequest = """
      {
        "deposit": {
          "id": "$nextDepositId",
          "amount": "$amount",
          "currency": "$currency"
        }
      }
    """

    webTestClient.put()
        .uri("/v1/wallets/{wallet-id}/deposit", walletId)
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(nextDepositRequest)
        .exchange()
        .expectStatus()
        .isNoContent

    val firstDepositResult = depositTestRepository.findDeposit(firstDepositId)
    assertNotNull(firstDepositResult)
    val nextDepositResult = depositTestRepository.findDeposit(nextDepositId)
    assertNotNull(nextDepositResult)
  }

  private fun createWallet(walletId: String) {
    walletTestRepository.createWallet(walletId)

    val ownerId = randomUUID().toString()
    val ownerUsername = getRandomAlphabetic(10)
    ownerTestRepository.createOwner(ownerId, ownerUsername, walletId)

    val accountId = randomUUID().toString()
    accountTestRepository.createAccount(
        accountEntityId = accountId,
        currency = "EUR",
        walletEntityId = walletId,
    )
  }
}
