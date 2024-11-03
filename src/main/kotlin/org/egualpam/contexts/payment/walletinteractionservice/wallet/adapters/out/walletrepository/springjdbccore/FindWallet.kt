package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Deposit
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
          val accounts = findAccounts(walletId)
              .map { mapAccount(it) }
              .toMutableSet()
          Wallet(
              id = walletId,
              owner = owner,
              accounts = accounts,
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

  private fun findAccounts(walletId: WalletId): MutableList<PersistenceAccountDto> {
    val queryAccount = """
        SELECT entity_id, currency
        FROM account
        WHERE wallet_entity_id=:walletId
      """

    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("walletId", walletId.value)

    val rowMapper = RowMapper<PersistenceAccountDto> { rs, _ ->
      rs.let {
        val id = rs.getString("entity_id")
        val currency = rs.getString("currency")
        PersistenceAccountDto(id, currency)
      }
    }

    return jdbcTemplate.query(
        queryAccount,
        sqlParameterSource,
        rowMapper,
    )
  }

  private fun mapAccount(account: PersistenceAccountDto): Account {
    val accountId = account.id
    val accountCurrency = account.currency

    val deposits = findDeposit(accountId)
        .map { it.toDomainEntity() }
        .let {
          val mutableSetOf = mutableSetOf<Deposit>()
          mutableSetOf.addAll(it)
          mutableSetOf
        }

    return Account.load(accountId, accountCurrency, deposits)
  }

  private fun findDeposit(accountId: String): MutableList<PersistenceDepositDto> {
    val queryDeposits = """
        SELECT entity_id, amount
        FROM deposit
        WHERE account_entity_id=:accountId
      """

    val queryDepositsParameterSource = MapSqlParameterSource()
    queryDepositsParameterSource.addValue("accountId", accountId)

    val rowMapper = RowMapper<PersistenceDepositDto> { rs, _ ->
      rs.let {
        val id = rs.getString("entity_id")
        val amount = rs.getDouble("amount")
        PersistenceDepositDto(id, amount)
      }
    }

    return jdbcTemplate.query(
        queryDeposits,
        queryDepositsParameterSource,
        rowMapper,
    )
  }

  data class PersistenceOwnerDto(val id: String, val username: String) {
    fun toDomainEntity() = Owner.load(id, username)
  }

  data class PersistenceAccountDto(val id: String, val currency: String)

  data class PersistenceDepositDto(val id: String, val amount: Double) {
    fun toDomainEntity() = Deposit.load(id, amount)
  }
}
