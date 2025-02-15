package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.time.Instant

class SaveWallet(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) {
  fun save(wallet: Wallet) {
    saveWallet(wallet)
    saveWalletOwner(wallet)
    wallet.accounts().forEach { account ->
      saveAccount(account, wallet.getId())
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
      INSERT IGNORE INTO owner(entity_id, created_at, username, wallet_entity_id)
      VALUES(:entityId, :createdAt, :username, :walletEntityId)
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
      INSERT IGNORE INTO account(entity_id, created_at, currency, wallet_entity_id)
      VALUES(:entityId, :createdAt, :currency, :walletEntityId)
    """
    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("entityId", account.getId().value)
    sqlParameterSource.addValue("createdAt", Instant.now())
    sqlParameterSource.addValue("currency", account.getCurrency().value)
    sqlParameterSource.addValue("walletEntityId", walletId.value)
    jdbcTemplate.update(sql, sqlParameterSource)
  }
}
