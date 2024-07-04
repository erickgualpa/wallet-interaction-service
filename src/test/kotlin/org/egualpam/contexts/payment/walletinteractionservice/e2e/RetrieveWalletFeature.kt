package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID.randomUUID

class RetrieveWalletFeature : AbstractIntegrationTest() {

  @Test
  fun `retrieve wallet`() {
    val walletId = randomUUID()
    val ownerId = "fake-owner-id"
    val accountId = "fake-account-id"

    mockMvc.perform(get("/v1/wallets/{wallet-id}", walletId))
        .andExpect(status().isOk)
        .andExpect(content().contentType("application/json"))
        .andExpect(
            content().json(
                """
                  {
                    "wallet": {
                      "id": "$walletId",
                      "owner": {
                        "id": "$ownerId"
                      },
                      "account": {
                        "id": "$accountId"
                      }
                    }
                  }
                """,
            ),
        )
  }
}
