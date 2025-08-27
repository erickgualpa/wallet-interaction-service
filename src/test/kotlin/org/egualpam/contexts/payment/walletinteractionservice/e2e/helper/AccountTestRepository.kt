package org.egualpam.contexts.payment.walletinteractionservice.e2e.helper

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class AccountTestRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) {
  fun createAccount(
    accountEntityId: String,
    currency: String,
    walletEntityId: String,
  ) {
    val sql = """
        INSERT INTO account(entity_id, balance, currency, wallet_entity_id, created_at)
        VALUES(:accountEntityId, :balance, :currency, :walletEntityId, :createdAt)
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("accountEntityId", accountEntityId)
    sqlParameters.addValue("balance", "0.0")
    sqlParameters.addValue("currency", currency)
    sqlParameters.addValue("walletEntityId", walletEntityId)
    sqlParameters.addValue("createdAt", Instant.now())

    jdbcTemplate.update(sql, sqlParameters)
  }

  fun findAccount(accountId: String): AccountResult? {
    val sql = """
        SELECT entity_id, balance, currency, created_at
        FROM account
        WHERE entity_id=:accountId
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("accountId", accountId)

    val accountResultRowMapper = RowMapper { rs, _ ->
      AccountResult(
          rs.getString("entity_id"),
          rs.getString("balance"),
          rs.getString("currency"),
          rs.getTimestamp("created_at").toInstant(),
      )
    }

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameters, accountResultRowMapper)
    } catch (_: EmptyResultDataAccessException) {
      null
    }
  }

  data class AccountResult(
    val id: String,
    val balance: String,
    val currency: String,
    val createdAt: Instant
  )
}
