package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.out.accountrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountBalance
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Transfer
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class SpringJdbcCoreAccountRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : AccountRepository {
  override fun find(id: AccountId): Account? {
    val queryAccount = """
        SELECT wallet_entity_id, balance, currency
        FROM account
        WHERE entity_id=:accountId
      """

    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("accountId", id.value)

    val rowMapper = RowMapper<PersistenceAccountDto> { rs, _ ->
      rs.let {
        val walletId = rs.getString("wallet_entity_id")
        val balance = rs.getString("balance")
        val currency = rs.getString("currency")
        PersistenceAccountDto(id.value, walletId, balance, currency)
      }
    }

    return try {
      val persistenceAccountDto =
          jdbcTemplate.queryForObject(queryAccount, sqlParameterSource, rowMapper)!!
      mapAccount(persistenceAccountDto)
    } catch (_: EmptyResultDataAccessException) {
      null
    }
  }

  private fun mapAccount(account: PersistenceAccountDto): Account {
    val accountId = account.id
    val accountBalance = account.balance.toDouble()
    val accountCurrency = account.currency
    val accountWalletId = account.walletId

    val deposits = findDeposit(accountId)
        .map { it.toDomainEntity() }
        .let {
          val mutableSetOf = mutableSetOf<Deposit>()
          mutableSetOf.addAll(it)
          mutableSetOf
        }

    val transfers = findTransfer(accountId)
        .map { it.toDomainEntity() }
        .let {
          val mutableSetOf = mutableSetOf<Transfer>()
          mutableSetOf.addAll(it)
          mutableSetOf
        }

    return Account.load(
        accountId,
        accountWalletId,
        accountCurrency,
        AccountBalance(accountBalance), // TODO: Avoid using value object
        deposits,
        transfers = transfers,
    )
  }

  private fun findDeposit(accountId: String): MutableList<PersistenceDepositDto> {
    val query = """
        SELECT entity_id, amount
        FROM deposit
        WHERE account_entity_id=:accountId
      """

    val parameterSource = MapSqlParameterSource()
    parameterSource.addValue("accountId", accountId)

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
        query,
        parameterSource,
        rowMapper,
    )
  }

  private fun findTransfer(accountId: String): MutableList<PersistenceTransferDto> {
    val query = """
        SELECT id, source_account_id, destination_account_id, amount
        FROM transfer
        WHERE source_account_id=:accountId OR destination_account_id=:accountId
      """

    val parameterSource = MapSqlParameterSource()
    parameterSource.addValue("accountId", accountId)

    val rowMapper = RowMapper<PersistenceTransferDto> { rs, _ ->
      rs.let {
        val id = rs.getString("id")
        val sourceAccountId = rs.getString("source_account_id")
        val destinationAccountId = rs.getString("destination_account_id")
        val amount = rs.getDouble("amount")
        val isInbound = destinationAccountId == accountId
        PersistenceTransferDto(
            id,
            sourceAccountId,
            destinationAccountId,
            amount,
            isInbound,
        )
      }
    }

    return jdbcTemplate.query(
        query,
        parameterSource,
        rowMapper,
    )
  }

  override fun save(account: Account) {
    insertIntoAccountTable(account)
    account.deposits().forEach {
      insertIntoDepositTable(it, account.getId())
    }
    account.transfers().forEach {
      insertIntoTransferTable(it)
    }
  }

  private fun insertIntoAccountTable(account: Account) {
    val sql = """
      INSERT INTO account(entity_id, created_at, balance, currency, wallet_entity_id)
      VALUES(:entityId, :createdAt, :balance, :currency, :walletEntityId)
      ON DUPLICATE KEY UPDATE 
        balance = VALUES(balance)
    """
    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("entityId", account.getId().value)
    sqlParameterSource.addValue("createdAt", Instant.now())
    sqlParameterSource.addValue("balance", account.balanceV2())
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

  private fun insertIntoTransferTable(transfer: Transfer) {
    val sql = """
      INSERT IGNORE INTO transfer(id, created_at, source_account_id, destination_account_id, amount)
      VALUES(:id, :createdAt, :sourceAccountId, :destinationAccountId, :amount)
    """
    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("id", transfer.getId().value)
    sqlParameterSource.addValue("createdAt", Instant.now())
    sqlParameterSource.addValue("sourceAccountId", transfer.sourceAccountId())
    sqlParameterSource.addValue("destinationAccountId", transfer.destinationAccountId())
    sqlParameterSource.addValue("amount", transfer.amount().value)
    jdbcTemplate.update(sql, sqlParameterSource)
  }


  private data class PersistenceAccountDto(
    val id: String,
    val walletId: String,
    val balance: String,
    val currency: String
  )

  private data class PersistenceDepositDto(val id: String, val amount: Double) {
    fun toDomainEntity() = Deposit.load(id, amount)
  }

  private data class PersistenceTransferDto(
    val id: String,
    val sourceAccountId: String,
    val destinationAccountId: String,
    val amount: Double,
    val isInbound: Boolean,
  ) {
    fun toDomainEntity() =
        Transfer.load(id, sourceAccountId, destinationAccountId, amount, isInbound)
  }
}
