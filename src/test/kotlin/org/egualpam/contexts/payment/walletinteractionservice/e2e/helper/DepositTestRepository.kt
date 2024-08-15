package org.egualpam.contexts.payment.walletinteractionservice.e2e.helper

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class DepositTestRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) {
  fun findDeposit(depositId: String): DepositResult? {
    val sql = """
        SELECT entity_id, created_at
        FROM deposit
        WHERE entity_id=:depositId
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("depositId", depositId)

    val depositResultRowMapper = RowMapper { rs, _ ->
      DepositResult(
          rs.getString("entity_id"),
          rs.getTimestamp("created_at").toInstant(),
      )
    }

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameters, depositResultRowMapper)
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }
}

data class DepositResult(val id: String, val createdAt: Instant)
