package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletsearchrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletSearchRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.WalletDto
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class SpringJdbcCoreWalletSearchRepository(
  private val jdbcTemplate: NamedParameterJdbcTemplate
) : WalletSearchRepository {
  override fun search(id: WalletId): WalletDto? {
    return findOwner(id.value)
        ?.let { owner -> WalletDto.OwnerDto(owner) }
        ?.let { owner ->
          val accounts = findAccounts(id)
              .map { WalletDto.AccountDto(it) }
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
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }

  private fun findAccounts(walletId: WalletId): MutableList<String> {
    val queryAccount = """
        SELECT entity_id
        FROM account
        WHERE wallet_entity_id=:walletId
      """

    val sqlParameterSource = MapSqlParameterSource()
    sqlParameterSource.addValue("walletId", walletId.value)

    return jdbcTemplate.queryForList(
        queryAccount,
        sqlParameterSource,
        String::class.java,
    )
  }
}
