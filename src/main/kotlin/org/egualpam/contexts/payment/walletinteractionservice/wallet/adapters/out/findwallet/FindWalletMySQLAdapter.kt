package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Owner
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.OwnerId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class FindWalletMySQLAdapter(
  private var jdbcTemplate: NamedParameterJdbcTemplate
) : FindWalletPort {
  override fun find(id: WalletId): Wallet? {
    val sql = """
      SELECT id, owner_id, account_id
      FROM wallet
      WHERE id=:walletId
    """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("walletId", id.value)

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameters, mapIntoWallet())
    } catch (e: EmptyResultDataAccessException) {
      return null
    }
  }

  private fun mapIntoWallet(): RowMapper<Wallet> {
    return RowMapper { rs, _ ->
      Wallet(
          WalletId(rs.getString("id")),
          Owner(OwnerId(rs.getString("owner_id"))),
          Account(AccountId(rs.getString("account_id"))),
      )
    }
  }
}
