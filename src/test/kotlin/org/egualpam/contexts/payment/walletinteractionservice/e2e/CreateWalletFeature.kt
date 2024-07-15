package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID.randomUUID
import kotlin.test.assertTrue

class CreateWalletFeature : AbstractIntegrationTest() {

  @Autowired
  private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

  @Test
  fun `create wallet`() {
    val walletId = randomUUID().toString()
    val ownerId = randomUUID().toString()
    val accountId = randomUUID().toString()

    val username = randomAlphabetic(10)
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

    mockMvc.perform(
        post("/v1/wallets")
            .contentType("application/json")
            .content(request),
    )
        .andExpect(status().isCreated)
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "/v1/wallet/$walletId"))

    assertTrue(walletExists(walletId))
  }

  private fun walletExists(walletId: String): Boolean {
    val sql = """
        SELECT COUNT(*)
        FROM wallet
        WHERE entity_id=:walletId
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("walletId", walletId)

    val count = jdbcTemplate.queryForObject(sql, sqlParameters, Int::class.java)

    return count == 1
  }
}
