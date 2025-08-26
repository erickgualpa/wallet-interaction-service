package org.egualpam.contexts.payment.walletinteractionservice.e2e.helper

import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.DepositMoney
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.DepositMoneyCommand
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.support.TransactionTemplate
import java.time.Instant

class DepositTestRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate,
  private val transactionTemplate: TransactionTemplate,
  private val depositMoney: DepositMoney
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
    } catch (_: EmptyResultDataAccessException) {
      null
    }
  }

  fun createDeposit(
    id: String,
    accountId: String,
    amount: Double,
    currency: String,
  ) {
    transactionTemplate.execute {
      val command = DepositMoneyCommand(id, amount, currency, accountId)
      depositMoney.execute(command)
    }
  }
}

data class DepositResult(val id: String, val createdAt: Instant)
