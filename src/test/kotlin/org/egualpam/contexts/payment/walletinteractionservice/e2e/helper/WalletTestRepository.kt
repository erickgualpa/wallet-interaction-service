package org.egualpam.contexts.payment.walletinteractionservice.e2e.helper

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class WalletTestRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) {
  fun createWallet(persistenceId: Int, entityId: String) {
    val sql = """
        INSERT INTO wallet(id, entity_id, created_at)
        VALUES(:persistenceId, :entityId, :createdAt)
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("persistenceId", persistenceId)
    sqlParameters.addValue("entityId", entityId)
    sqlParameters.addValue("createdAt", Instant.now())

    jdbcTemplate.update(sql, sqlParameters)
  }

  fun findWallet(walletId: String): WalletResult? {
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
}

data class WalletResult(val id: String, val createdAt: Instant)
