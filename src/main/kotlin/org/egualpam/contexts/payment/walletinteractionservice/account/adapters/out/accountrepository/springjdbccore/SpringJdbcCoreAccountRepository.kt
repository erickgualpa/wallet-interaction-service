package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.out.accountrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class SpringJdbcCoreAccountRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : AccountRepository {
  override fun find(id: AccountId): Account? {
    val queryAccount = """
        SELECT wallet_entity_id, currency
        FROM account
        WHERE entity_id=:accountId
      """

    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("accountId", id.value)

    val rowMapper = RowMapper<PersistenceAccountDto> { rs, _ ->
      rs.let {
        val walletId = rs.getString("wallet_entity_id")
        val currency = rs.getString("currency")
        PersistenceAccountDto(id.value, walletId, currency)
      }
    }

    return try {
      val persistenceAccountDto =
          jdbcTemplate.queryForObject(queryAccount, sqlParameterSource, rowMapper)!!
      mapAccount(persistenceAccountDto)
    } catch (e: Exception) {
      null
    }
  }

  private fun mapAccount(account: PersistenceAccountDto): Account {
    val accountId = account.id
    val accountCurrency = account.currency
    val accountWalletId = account.walletId

    val deposits = findDeposit(accountId)
        .map { it.toDomainEntity() }
        .let {
          val mutableSetOf = mutableSetOf<Deposit>()
          mutableSetOf.addAll(it)
          mutableSetOf
        }

    return Account.load(accountId, accountWalletId, accountCurrency, deposits)
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
        PersistenceDepositDto(
            id,
            amount,
        )
      }
    }

    return jdbcTemplate.query(
        queryDeposits,
        queryDepositsParameterSource,
        rowMapper,
    )
  }

  override fun save(account: Account) {
    insertIntoAccountTable(account)
    account.deposits().forEach {
      insertIntoDepositTable(it, account.getId())
    }
  }

  private fun insertIntoAccountTable(account: Account) {
    val sql = """
      INSERT IGNORE INTO account(entity_id, created_at, currency, wallet_entity_id)
      VALUES(:entityId, :createdAt, :currency, :walletEntityId)
    """
    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("entityId", account.getId().value)
    sqlParameterSource.addValue("createdAt", Instant.now())
    sqlParameterSource.addValue("currency", account.currency().value)
    sqlParameterSource.addValue("walletEntityId", account.walletId().value)
    jdbcTemplate.update(sql, sqlParameterSource)
  }

  private fun insertIntoDepositTable(deposit: Deposit, accountId: AccountId) {
    val sql = """
      INSERT IGNORE INTO deposit(entity_id, created_at, amount, account_entity_id)
      VALUES(:entityId, :createdAt, :amount, :accountEntityId)
    """
    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("entityId", deposit.getId().value)
    sqlParameterSource.addValue("createdAt", Instant.now())
    sqlParameterSource.addValue("amount", deposit.amount().value)
    sqlParameterSource.addValue("accountEntityId", accountId.value)
    jdbcTemplate.update(sql, sqlParameterSource)
  }

  private data class PersistenceAccountDto(
    val id: String,
    val walletId: String,
    val currency: String
  )

  private data class PersistenceDepositDto(val id: String, val amount: Double) {
    fun toDomainEntity() = Deposit.load(id, amount)
  }
}
