package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.depositexists

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.DepositId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.DepositExists
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class DepositExistsMySQLAdapter(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : DepositExists {
  override fun with(depositId: DepositId): Boolean {
    val sql = """
      SELECT COUNT(*)
      FROM deposit
      WHERE entity_id=:depositId
    """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("depositId", depositId.value)

    return jdbcTemplate.queryForObject(sql, sqlParameters, Int::class.java) == 1
  }
}
