package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.out.accountexists

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountExists
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class AccountExistsMySQLAdapter(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : AccountExists {
  override fun with(id: AccountId): Boolean {
    val sql = """
      SELECT COUNT(*)
      FROM account
      WHERE entity_id=:accountId
    """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("accountId", id.value)

    return jdbcTemplate.queryForObject(sql, sqlParameters, Int::class.java) == 1
  }
}
