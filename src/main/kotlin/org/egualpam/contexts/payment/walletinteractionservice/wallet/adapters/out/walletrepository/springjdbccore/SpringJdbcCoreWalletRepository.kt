package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Owner
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class SpringJdbcCoreWalletRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : WalletRepository {
  override fun find(walletId: WalletId): Wallet? {
    return findOwner(walletId.value)?.toDomainEntity()?.let { owner ->
      val accounts = findAccounts(walletId).map { mapAccount(it) }.toMutableSet()
      Wallet(
          id = walletId,
          owner = owner,
          accounts = accounts,
      )
    }
  }

  private fun mapAccount(account: PersistenceAccountDto): Account {
    val accountId = account.id
    val accountCurrency = account.currency

    val deposits = findDeposit(accountId).map { it.toDomainEntity() }.let {
      val mutableSetOf = mutableSetOf<Deposit>()
      mutableSetOf.addAll(it)
      mutableSetOf
    }

    return Account.load(accountId, accountCurrency, deposits)
  }

  private fun findOwner(walletId: String): PersistenceOwnerDto? {
    val queryOwner = """
        SELECT entity_id, username
        FROM owner
        WHERE wallet_entity_id=:walletId
      """

    val queryOwnerParameterSource = MapSqlParameterSource()
    queryOwnerParameterSource.addValue("walletId", walletId)

    val ownerDtoRowMapper = RowMapper<PersistenceOwnerDto> { rs, _ ->
      rs.let {
        val id = rs.getString("entity_id")
        val username = rs.getString("username")
        PersistenceOwnerDto(id, username)
      }
    }

    return try {
      jdbcTemplate.queryForObject(queryOwner, queryOwnerParameterSource, ownerDtoRowMapper)
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

    val queryAccountParameterSource = MapSqlParameterSource()
    queryAccountParameterSource.addValue("walletId", walletId.value)

    val rowMapper = RowMapper<PersistenceAccountDto> { rs, _ ->
      rs.let {
        val id = rs.getString("entity_id")
        val currency = rs.getString("currency")
        PersistenceAccountDto(id, currency)
      }
    }

    return jdbcTemplate.query(
        queryAccount,
        queryAccountParameterSource,
        rowMapper,
    )
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

  override fun save(wallet: Wallet) {
    saveWallet(wallet)
    saveWalletOwner(wallet)
    wallet.accounts().forEach { account ->
      saveAccount(account, wallet.getId())
      account.deposits().forEach { deposit ->
        saveDeposit(deposit, account.getId())
      }
    }
  }

  private fun saveWallet(wallet: Wallet) {
    val sql = """
      INSERT IGNORE INTO wallet(entity_id, created_at)
      VALUES(:entityId, :createdAt)
    """
    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("entityId", wallet.getId().value)
    sqlParameterSource.addValue("createdAt", Instant.now())
    jdbcTemplate.update(sql, sqlParameterSource)
  }

  private fun saveWalletOwner(wallet: Wallet) {
    val sql = """
      INSERT IGNORE INTO owner(entity_id, created_at, username, wallet_entity_id, wallet)
      SELECT :entityId, :createdAt, :username, :walletEntityId, id
      FROM wallet
      WHERE entity_id=:walletEntityId
    """
    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("entityId", wallet.getOwnerId().value)
    sqlParameterSource.addValue("createdAt", Instant.now())
    sqlParameterSource.addValue("username", wallet.getOwnerUsername().value)
    sqlParameterSource.addValue("walletEntityId", wallet.getId().value)
    jdbcTemplate.update(sql, sqlParameterSource)
  }

  private fun saveAccount(account: Account, walletId: WalletId) {
    val sql = """
      INSERT IGNORE INTO account(entity_id, created_at, currency, wallet_entity_id, wallet)
      SELECT :entityId, :createdAt, :currency, :walletEntityId, id
      FROM wallet
      WHERE entity_id=:walletEntityId
    """
    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("entityId", account.getId().value)
    sqlParameterSource.addValue("createdAt", Instant.now())
    sqlParameterSource.addValue("currency", account.getCurrency().value)
    sqlParameterSource.addValue("walletEntityId", walletId.value)
    jdbcTemplate.update(sql, sqlParameterSource)
  }

  private fun saveDeposit(deposit: Deposit, accountId: AccountId) {
    val sql = """
      INSERT IGNORE INTO deposit(entity_id, created_at, amount, account_entity_id, account)
      SELECT :entityId, :createdAt, :amount, :accountEntityId, id
      FROM account
      WHERE entity_id=:accountEntityId
    """
    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("entityId", deposit.getId().value)
    sqlParameterSource.addValue("createdAt", Instant.now())
    sqlParameterSource.addValue("amount", deposit.amount().value)
    sqlParameterSource.addValue("accountEntityId", accountId.value)
    jdbcTemplate.update(sql, sqlParameterSource)
  }

}
