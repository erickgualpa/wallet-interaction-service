package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.TemporalUnitWithinOffset
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
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

    val walletResult = findWallet(walletId)
    assertNotNull(walletResult)
    assertThat(walletResult).satisfies(
        {
          assertEquals(walletId, it.id)
          assertThat(it.createdAt).isCloseTo(
              Instant.now(),
              TemporalUnitWithinOffset(1, SECONDS),
          )
        },
    )

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

    val accountResult = findAccount(accountId)
    assertNotNull(accountResult)
    assertThat(accountResult).satisfies(
        {
          assertEquals(accountId, it.id)
          assertEquals(currency, it.currency)
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

  private fun findWallet(walletId: String): WalletResult? {
    val sql = """
        SELECT entity_id, created_at
        FROM wallet
        WHERE entity_id=:walletId
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("walletId", walletId)

    val walletResultRowMapper = RowMapper { rs, _ ->
      WalletResult(
          rs.getString("entity_id"),
          rs.getTimestamp("created_at").toInstant(),
      )
    }

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameters, walletResultRowMapper)
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }

  private fun findOwner(ownerId: String): OwnerResult? {
    val sql = """
        SELECT entity_id, username, created_at
        FROM owner
        WHERE entity_id=:walletId
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("walletId", ownerId)

    val ownerResultRowMapper = RowMapper { rs, _ ->
      OwnerResult(
          rs.getString("entity_id"),
          rs.getString("username"),
          rs.getTimestamp("created_at").toInstant(),
      )
    }

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameters, ownerResultRowMapper)
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }

  private fun findAccount(accountId: String): AccountResult? {
    val sql = """
        SELECT entity_id, currency, created_at
        FROM account
        WHERE entity_id=:accountId
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("accountId", accountId)

    val accountResultRowMapper = RowMapper { rs, _ ->
      AccountResult(
          rs.getString("entity_id"),
          rs.getString("currency"),
          rs.getTimestamp("created_at").toInstant(),
      )
    }

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameters, accountResultRowMapper)
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }
}

data class WalletResult(val id: String, val createdAt: Instant)

data class OwnerResult(val id: String, val username: String, val createdAt: Instant)

data class AccountResult(val id: String, val currency: String, val createdAt: Instant)
