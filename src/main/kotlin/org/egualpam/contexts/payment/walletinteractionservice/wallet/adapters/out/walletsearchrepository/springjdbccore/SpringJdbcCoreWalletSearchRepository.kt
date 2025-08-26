package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletsearchrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.query.RetrieveAccountBalance
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.query.RetrieveAccountBalanceQuery
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.`in`.query.WalletDto
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletSearchRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class SpringJdbcCoreWalletSearchRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate,
  private val retrieveAccountBalance: RetrieveAccountBalance
) : WalletSearchRepository {
  override fun search(id: WalletId): WalletDto? {
    return findOwner(id.value)
        ?.let { owner -> WalletDto.OwnerDto(owner) }
        ?.let { owner ->
          val accounts = findAccounts(id)
              .map {
                val query = RetrieveAccountBalanceQuery(it.id)
                val accountBalance = retrieveAccountBalance.execute(query)
                WalletDto.AccountDto(
                    it.id,
                    balance = accountBalance.balance,
                    it.currency,
                )
              }
              .toMutableSet()
          WalletDto(
              id = id.value,
              owner = owner,
              accounts = accounts,
          )
        }
  }

  private fun findOwner(walletId: String): String? {
    val sql = """
        SELECT entity_id
        FROM owner
        WHERE wallet_entity_id=:walletId
      """

    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("walletId", walletId)

    return try {
      jdbcTemplate.queryForObject(sql, sqlParameterSource, String::class.java)
    } catch (_: EmptyResultDataAccessException) {
      null
    }
  }

  private fun findAccounts(walletId: WalletId): MutableList<PersistenceAccountDto> {
    val query = """
        SELECT entity_id, currency
        FROM account
        WHERE wallet_entity_id=:walletId
      """

    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("walletId", walletId.value)

    val rowMapper = RowMapper { rs, _ ->
      rs.let {
        val id = rs.getString("entity_id")
        val currency = rs.getString("currency")
        PersistenceAccountDto(id, currency)
      }
    }

    return jdbcTemplate.query(
        query,
        sqlParameterSource,
        rowMapper,
    )
  }

  data class PersistenceAccountDto(val id: String, val currency: String)
}
