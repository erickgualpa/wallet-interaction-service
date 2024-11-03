package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class SpringJdbcCoreWalletRepository(
  jdbcTemplate: NamedParameterJdbcTemplate
) : WalletRepository {
  private val findWallet = FindWallet(jdbcTemplate)
  private val saveWallet = SaveWallet(jdbcTemplate)

  override fun find(walletId: WalletId): Wallet? = findWallet.find(walletId)
  override fun save(wallet: Wallet) = saveWallet.save(wallet)
}
