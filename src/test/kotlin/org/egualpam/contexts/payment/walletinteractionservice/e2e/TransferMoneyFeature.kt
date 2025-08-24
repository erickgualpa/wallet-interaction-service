package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphabetic
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.com.google.common.net.HttpHeaders.CONTENT_TYPE
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextDouble

class TransferMoneyFeature : AbstractIntegrationTest() {

  companion object {
    private const val CURRENCY = "EUR"
  }

  @Test
  fun `transfer money from one account to another`() {
    val transferId = randomUUID()

    val originWalletId = randomUUID().toString()
    val originAccountId = randomUUID().toString()
    createWallet(originWalletId, originAccountId)

    // Create the destination account
    val destinationWalletId = randomUUID().toString()
    val destinationAccountId = randomUUID().toString()
    createWallet(destinationWalletId, destinationAccountId)

    val transferAmount = nextDouble(10.0, 10000.0)

    val request = """
      {
        "id": "$transferId",
        "destinationAccountId": "$destinationAccountId",
        "amount": "$transferAmount",
        "currency": "$CURRENCY"
      }
    """

    webTestClient.put()
        .uri("/v1/accounts/{account-id}/transfers", originAccountId)
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isNoContent
  }

  // TODO: Add test with concurrent transfers

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
