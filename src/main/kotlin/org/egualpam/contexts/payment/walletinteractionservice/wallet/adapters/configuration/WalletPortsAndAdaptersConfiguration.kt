package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletFakeAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletMySQLAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletSpringDataJdbcAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.savewallet.WalletRepositorySpringDataJdbcAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletCrudRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class WalletPortsAndAdaptersConfiguration {
  @Bean
  fun findWalletFakeAdapter(): FindWallet = FindWalletFakeAdapter()

  @Bean
  fun findWalletMySQLAdapter(
    jdbcTemplate: NamedParameterJdbcTemplate
  ): FindWallet = FindWalletMySQLAdapter(jdbcTemplate)

  @Primary
  @Bean
  fun findWalletSpringDataJdbcAdapter(
    walletCrudRepository: WalletCrudRepository
  ): FindWallet = FindWalletSpringDataJdbcAdapter(walletCrudRepository)

  @Bean
  fun walletRepositorySpringDataJdbcAdapter(
    walletCrudRepository: WalletCrudRepository
  ): WalletRepository = WalletRepositorySpringDataJdbcAdapter(walletCrudRepository)

  @Bean
  fun walletExistsFakeAdapter(): WalletExists {
    return object : WalletExists {
      override fun with(walletId: WalletId) = false
    }
  }
}
