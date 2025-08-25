package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphabetic
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.testcontainers.shaded.com.google.common.net.HttpHeaders.CONTENT_TYPE
import java.util.UUID.randomUUID
import java.util.concurrent.Executors
import kotlin.random.Random.Default.nextDouble

class TransferMoneyFeature : AbstractIntegrationTest() {

  companion object {
    private const val CURRENCY = "EUR"
  }

  @Test
  fun `transfer money from one account to another`() {
    val transferId = randomUUID().toString()

    val sourceWalletId = randomUUID().toString()
    val sourceAccountId = randomUUID().toString()
    createWallet(sourceWalletId, sourceAccountId)

    // Create the destination account
    val destinationWalletId = randomUUID().toString()
    val destinationAccountId = randomUUID().toString()
    createWallet(destinationWalletId, destinationAccountId)

    val transferAmount = nextDouble(10.0, 10000.0)

    val request = """
      {
        "id": "$transferId",
        "destinationAccountId": "$destinationAccountId",
        "amount": $transferAmount
      }
    """

    webTestClient.put()
        .uri("/v1/accounts/{account-id}/transfers", sourceAccountId)
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isNoContent

    val transferResult = transferTestRepository.findTransfer(transferId)
    assertNotNull(transferResult)
  }

  @Test
  fun `transfer money from one account to another concurrently`() {
    val transferIdsPool = setOf(
        randomUUID().toString(),
        randomUUID().toString(),
        randomUUID().toString(),
        randomUUID().toString(),
    )

    val sourceWalletId = randomUUID().toString()
    val sourceAccountId = randomUUID().toString()
    createWallet(sourceWalletId, sourceAccountId)

    // Create the destination account
    val destinationWalletId = randomUUID().toString()
    val destinationAccountId = randomUUID().toString()
    createWallet(destinationWalletId, destinationAccountId)

    val transferAmount = 100.00

    val es = Executors.newFixedThreadPool(4)

    es.use {
      transferIdsPool.forEach { transferId ->
        es.submit {
          val request = """
          {
            "id": "$transferId",
            "destinationAccountId": "$destinationAccountId",
            "amount": $transferAmount
          }
        """

          webTestClient.put()
              .uri("/v1/accounts/{account-id}/transfers", sourceAccountId)
              .header(CONTENT_TYPE, "application/json")
              .bodyValue(request)
              .exchange()
              .expectStatus()
              .isNoContent
        }
      }
    }

    Thread.sleep(1000)

    transferIdsPool.forEach { transferId ->
      val transferResult = transferTestRepository.findTransfer(transferId)
      assertNotNull(transferResult)
    }
  }

  private fun createWallet(walletId: String, accountId: String) {
    walletTestRepository.createWallet(walletId)

    val ownerId = randomUUID().toString()
    val ownerUsername = getRandomAlphabetic(10)
    ownerTestRepository.createOwner(ownerId, ownerUsername, walletId)

    accountTestRepository.createAccount(
        accountEntityId = accountId,
        currency = CURRENCY,
        walletEntityId = walletId,
    )
  }
}
