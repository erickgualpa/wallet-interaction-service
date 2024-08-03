package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class WalletExistsMySQLAdapter(
  private var jdbcTemplate: NamedParameterJdbcTemplate
) : WalletExists {
  override fun with(walletId: WalletId): Boolean {
    val sql = """
      SELECT COUNT(*)
      FROM wallet
      WHERE id=:walletId
    """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("walletId", walletId.value)

    return jdbcTemplate.queryForObject(sql, sqlParameters, Int::class.java) == 1
  }
}
