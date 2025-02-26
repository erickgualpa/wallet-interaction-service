package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Owner
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class FindWallet(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) {
  fun find(walletId: WalletId): Wallet? {
    return findOwner(walletId.value)
        ?.toDomainEntity()
        ?.let { owner ->
          Wallet(
              id = walletId,
              owner = owner,
          )
        }
  }

  private fun findOwner(walletId: String): PersistenceOwnerDto? {
    val sql = """
        SELECT entity_id, username
        FROM owner
        WHERE wallet_entity_id=:walletId
      """

    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("walletId", walletId)

    val rowMapper = RowMapper<PersistenceOwnerDto> { rs, _ ->
      rs.let {
        val id = rs.getString("entity_id")
        val username = rs.getString("username")
        PersistenceOwnerDto(id, username)
      }
    }

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper)
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }

  data class PersistenceOwnerDto(val id: String, val username: String) {
    fun toDomainEntity() = Owner.load(id, username)
  }

  data class PersistenceAccountDto(val id: String, val currency: String)
}
