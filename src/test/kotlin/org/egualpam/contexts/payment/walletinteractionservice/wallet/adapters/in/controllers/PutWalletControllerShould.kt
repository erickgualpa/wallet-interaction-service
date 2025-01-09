package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.WalletInteractionServiceApplication
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidAggregateId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.shared.helper.RandomValuesSupplier.Companion.getRandomAlphabetic
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.OwnerUsername
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.AccountCurrencyIsNotSupported
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.OwnerUsernameAlreadyExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command.CreateWallet
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.willThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import org.springframework.transaction.support.TransactionTemplate
import java.util.UUID.randomUUID

@ContextConfiguration(classes = [WalletInteractionServiceApplication::class])
@WebMvcTest(PutWalletController::class)
class PutWalletControllerShould {

  @MockitoBean
  private lateinit var transactionTemplate: TransactionTemplate

  @MockitoBean
  private lateinit var createWallet: CreateWallet

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Test
  fun `get 409 CONFLICT use case throws OwnerUsernameAlreadyExists`() {
    val existingOwnerUsername = getRandomAlphabetic(5)

    given {
      transactionTemplate.executeWithoutResult(any())
    } willThrow { OwnerUsernameAlreadyExists(OwnerUsername(existingOwnerUsername)) }

    val request = """
      {
        "wallet": {
          "id": "${randomUUID()}",
          "owner": {
            "id": "${randomUUID()}",
            "username": "$existingOwnerUsername"
          },
          "account": {
            "id": "${randomUUID()}",
            "currency": "EUR"
          }
        }
      }
    """

    mockMvc.put("/v1/wallets") {
      contentType = APPLICATION_JSON
      content = request
    }.andExpect {
      status { isConflict() }
    }
  }

  @Test
  fun `get 400 BAD REQUEST when use case throws InvalidAggregateId`() {
    val invalidWalletId = getRandomAlphabetic(10)

    given {
      transactionTemplate.executeWithoutResult(any())
    } willThrow { InvalidAggregateId(invalidWalletId) }

    val request = """
      {
        "wallet": {
          "id": "$invalidWalletId",
          "owner": {
            "id": "${randomUUID()}",
            "username": "${getRandomAlphabetic(10)}"
          },
          "account": {
            "id": "${randomUUID()}",
            "currency": "EUR"
          }
        }
      }
    """

    mockMvc.put("/v1/wallets") {
      contentType = APPLICATION_JSON
      content = request
    }.andExpect {
      status { isBadRequest() }
    }
  }

  @Test
  fun `get 400 BAD REQUEST when use case throws InvalidDomainEntityId`() {
    val invalidOwnerId = getRandomAlphabetic(10)

    given {
      transactionTemplate.executeWithoutResult(any())
    } willThrow { InvalidDomainEntityId(invalidOwnerId) }

    val request = """
      {
        "wallet": {
          "id": "${randomUUID()}",
          "owner": {
            "id": "$invalidOwnerId",
            "username": "${getRandomAlphabetic(10)}"
          },
          "account": {
            "id": "${randomUUID()}",
            "currency": "EUR"
          }
        }
      }
    """

    mockMvc.put("/v1/wallets") {
      contentType = APPLICATION_JSON
      content = request
    }.andExpect {
      status { isBadRequest() }
    }
  }

  @Test
  fun `get 400 BAD REQUEST when use case throws AccountCurrencyIsNotSupported`() {
    val unsupportedCurrency = "USD"

    given {
      transactionTemplate.executeWithoutResult(any())
    } willThrow { AccountCurrencyIsNotSupported(unsupportedCurrency) }

    val request = """
      {
        "wallet": {
          "id": "${randomUUID()}",
          "owner": {
            "id": "${randomUUID()}",
            "username": "${getRandomAlphabetic(10)}"
          },
          "account": {
            "id": "${randomUUID()}",
            "currency": "$unsupportedCurrency"
          }
        }
      }
    """

    mockMvc.put("/v1/wallets") {
      contentType = APPLICATION_JSON
      content = request
    }.andExpect {
      status { isBadRequest() }
    }
  }
}
