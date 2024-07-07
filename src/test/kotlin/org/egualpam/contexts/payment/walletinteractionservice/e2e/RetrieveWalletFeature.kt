package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.UUID.randomUUID

class RetrieveWalletFeature : AbstractIntegrationTest() {

  @Autowired
  private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

  @Test
  fun `retrieve wallet`() {
    val walletId = randomUUID().toString()
    val ownerId = randomUUID().toString()
    val accountId = randomUUID().toString()

    createWallet(walletId, ownerId, accountId)

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

  private fun createWallet(walletId: String, ownerId: String, accountId: String) {
    val sql = """
        INSERT INTO wallet(id, owner_id, account_id, created_at)
        VALUES(:walletId, :ownerId, :accountId, :createdAt)
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("walletId", walletId)
    sqlParameters.addValue("ownerId", ownerId)
    sqlParameters.addValue("accountId", accountId)
    sqlParameters.addValue("createdAt", Instant.now())

    jdbcTemplate.update(sql, sqlParameters)
  }
}
