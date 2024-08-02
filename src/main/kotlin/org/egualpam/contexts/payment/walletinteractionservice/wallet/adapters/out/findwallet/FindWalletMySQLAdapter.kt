package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.WalletDto
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class FindWalletMySQLAdapter(
  private var jdbcTemplate: NamedParameterJdbcTemplate
) : FindWallet {
  override fun find(id: WalletId): WalletDto? {
    val sql = """
      SELECT id, owner_id, account_id
      FROM wallet
      WHERE id=:walletId
    """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("walletId", id.value)

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameters, mapIntoWalletDto())
    } catch (e: EmptyResultDataAccessException) {
      return null
    }
  }

  private fun mapIntoWalletDto(): RowMapper<WalletDto> {
    return RowMapper { rs, _ ->
      WalletDto(
          rs.getString("id"),
          WalletDto.OwnerDto(rs.getString("owner_id")),
          WalletDto.AccountDto(rs.getString("account_id")),
      )
    }
  }
}
