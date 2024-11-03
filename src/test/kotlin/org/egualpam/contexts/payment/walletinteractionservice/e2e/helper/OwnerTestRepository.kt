package org.egualpam.contexts.payment.walletinteractionservice.e2e.helper

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class OwnerTestRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) {
  fun createOwner(
    ownerEntityId: String,
    ownerUsername: String,
    walletEntityId: String,
  ) {
    val sql = """
        INSERT INTO owner(entity_id, username, wallet_entity_id, created_at)
        VALUES(:ownerEntityId, :username, :walletEntityId, :createdAt)
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("ownerEntityId", ownerEntityId)
    sqlParameters.addValue("username", ownerUsername)
    sqlParameters.addValue("walletEntityId", walletEntityId)
    sqlParameters.addValue("createdAt", Instant.now())

    jdbcTemplate.update(sql, sqlParameters)
  }

  fun findOwner(ownerId: String): OwnerResult? {
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

  data class OwnerResult(val id: String, val username: String, val createdAt: Instant)
}
