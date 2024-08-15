package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletexists

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.OwnerUsername
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class WalletExistsMySQLAdapter(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : WalletExists {
  override fun with(walletId: WalletId): Boolean {
    val sql = """
      SELECT COUNT(*)
      FROM wallet
      WHERE entity_id=:walletId
    """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("walletId", walletId.value)

    return jdbcTemplate.queryForObject(sql, sqlParameters, Int::class.java) == 1
  }

  override fun with(ownerUsername: OwnerUsername): Boolean {
    val sql = """
      SELECT COUNT(*)
      FROM owner
      WHERE username=:ownerUsername
    """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("ownerUsername", ownerUsername.value)

    return jdbcTemplate.queryForObject(sql, sqlParameters, Int::class.java) == 1
  }
}
