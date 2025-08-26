package org.egualpam.contexts.payment.walletinteractionservice.journey

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphabetic
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.com.google.common.net.HttpHeaders.CONTENT_TYPE
import java.util.UUID.randomUUID

class TransferJourneyTest : AbstractIntegrationTest() {

  companion object {
    private const val CURRENCY = "EUR"
  }

  @Test
  fun `transfer journey`() {
    val sourceWalletId = randomUUID().toString()
    val sourceAccountId = randomUUID()

    val destinationWalletId = randomUUID().toString()
    val destinationAccountId = randomUUID()

    // STEP 1 -> Create source wallet
    val createSourceWalletRequest = """
      {
        "wallet": {
          "id": "$sourceWalletId",
          "owner": {
            "id": "${randomUUID()}",
            "username": "${getRandomAlphabetic(10)}"
          },
          "account": {
            "id": "$sourceAccountId",
            "currency": "$CURRENCY"
          }
        }
      }
    """

    webTestClient.put()
        .uri("/v1/wallets")
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(createSourceWalletRequest)
        .exchange()
        .expectStatus()
        .isNoContent

    waitForAccountProcessing()

    webTestClient.get()
        .uri("/v1/wallets/{wallet-id}", sourceWalletId)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody()
        .json(
            """
              {
                "wallet": {
                  "accounts": [
                    {
                      "id": "$sourceAccountId",
                      "balance": "0.0",
                      "currency": "$CURRENCY"
                    }
                  ]
                }
              }
            """,
        )

    // STEP 2 -> Create destination wallet
    val createDestinationWalletRequest = """
      {
        "wallet": {
          "id": "$destinationWalletId",
          "owner": {
            "id": "${randomUUID()}",
            "username": "${getRandomAlphabetic(10)}"
          },
          "account": {
            "id": "$destinationAccountId",
            "currency": "$CURRENCY"
          }
        }
      }
    """

    webTestClient.put()
        .uri("/v1/wallets")
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(createDestinationWalletRequest)
        .exchange()
        .expectStatus()
        .isNoContent

    webTestClient.get()
        .uri("/v1/wallets/{wallet-id}", sourceWalletId)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody()
        .json(
            """
              {
                "wallet": {
                  "accounts": [
                    {
                      "id": "$sourceAccountId",
                      "balance": "0.0",
                      "currency": "$CURRENCY"
                    }
                  ]
                }
              }
            """,
        )

    // STEP 3 -> Deposit money into source account
    val depositAmount = 10000.00
    val accountDepositRequest = """
      {
        "id": "${randomUUID()}",
        "amount": "$depositAmount",
        "currency": "$CURRENCY"
      }
    """

    webTestClient.put()
        .uri("/v1/accounts/{account-id}/deposits", sourceAccountId)
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(accountDepositRequest)
        .exchange()
        .expectStatus()
        .isNoContent

    // STEP 4 -> Check balance from the source account
    webTestClient.get()
        .uri("/v1/wallets/{wallet-id}", sourceWalletId)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody()
        .json(
            """
              {
                "wallet": {
                  "accounts": [
                    {
                      "id": "$sourceAccountId",
                      "balance": "$depositAmount",
                      "currency": "$CURRENCY"
                    }
                  ]
                }
              }
            """,
        )

    // STEP 5 -> Transfer money from source account to destination account
    val transferId = randomUUID()
    val transferAmount = 7500.00
    val transferMoneyRequest = """
      {
        "id": "$transferId",
        "destinationAccountId": "$destinationAccountId",
        "amount": $transferAmount
      }
    """
    webTestClient.put()
        .uri("/v1/accounts/{account-id}/transfers", sourceAccountId)
        .header(CONTENT_TYPE, "application/json")
        .bodyValue(transferMoneyRequest)
        .exchange()
        .expectStatus()
        .isNoContent

    // STEP 6 -> Check balance from both accounts
    val expectedSourceAccountBalance = depositAmount - transferAmount
    val expectedDestinationAccountBalance = transferAmount
    webTestClient.get()
        .uri("/v1/wallets/{wallet-id}", sourceWalletId)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody()
        .json(
            """
              {
                "wallet": {
                  "accounts": [
                    {
                      "id": "$sourceAccountId",
                      "balance": "$expectedSourceAccountBalance",
                      "currency": "$CURRENCY"
                    }
                  ]
                }
              }
            """,
        )

    webTestClient.get()
        .uri("/v1/wallets/{wallet-id}", destinationWalletId)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody()
        .json(
            """
              {
                "wallet": {
                  "accounts": [
                    {
                      "id": "$destinationAccountId",
                      "balance": "$expectedDestinationAccountBalance",
                      "currency": "$CURRENCY"
                    }
                  ]
                }
              }
            """,
        )
  }

  private fun waitForAccountProcessing() {
    Thread.sleep(100)
  }
}
