package org.egualpam.contexts.payment.walletinteractionservice.e2e.helper

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class TransferTestRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) {
  fun findTransfer(transferId: String): TransferResult? {
    val sql = """
        SELECT id, created_at
        FROM transfer
        WHERE id=:transferId
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("transferId", transferId)

    val rowMapper = RowMapper { rs, _ ->
      TransferResult(
          rs.getString("id"),
          rs.getTimestamp("created_at").toInstant(),
      )
    }

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameters, rowMapper)
    } catch (_: EmptyResultDataAccessException) {
      null
    }
  }
}

data class TransferResult(val id: String, val createdAt: Instant)
