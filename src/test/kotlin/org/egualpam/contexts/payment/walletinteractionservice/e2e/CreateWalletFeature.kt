package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.TemporalUnitWithinOffset
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.time.temporal.ChronoUnit.SECONDS
import java.util.UUID.randomUUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
        put("/v1/wallets")
            .contentType("application/json")
            .content(request),
    ).andExpect(status().isNoContent)

    assertTrue(walletExists(walletId))

    val ownerResult = findOwner(ownerId)
    assertNotNull(ownerResult)
    assertThat(ownerResult).satisfies(
        {
          assertEquals(ownerId, it.id)
          assertEquals(username, it.username)
          assertThat(it.createdAt).isCloseTo(
              Instant.now(),
              TemporalUnitWithinOffset(1, SECONDS),
          )
        },
    )
  }

  @Test
  fun `get 400 BAD REQUEST when domain entity id is invalid`() {
    val invalidWalletId = randomAlphabetic(10)
    val ownerId = randomUUID().toString()
    val accountId = randomUUID().toString()

    val username = randomAlphabetic(10)
    val currency = "EUR"

    val request = """
      {
        "wallet": {
          "id": "$invalidWalletId",
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
        put("/v1/wallets")
            .contentType("application/json")
            .content(request),
    ).andExpect(status().isBadRequest)
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

  private fun findOwner(ownerId: String): OwnerResult? {
    val sql = """
        SELECT entity_id, username, created_at
        FROM owner
        WHERE entity_id=:walletId
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("walletId", ownerId)

    return jdbcTemplate.queryForObject(sql, sqlParameters, mapIntoOwnerResult())
  }

  private fun mapIntoOwnerResult(): RowMapper<OwnerResult> {
    return RowMapper { rs, _ ->
      OwnerResult(
          rs.getString("entity_id"),
          rs.getString("username"),
          rs.getTimestamp("created_at").toInstant(),
      )
    }
  }
}

data class OwnerResult(val id: String, val username: String, val createdAt: Instant)
