package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletCrudRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletexists.WalletExistsMySQLAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository.WalletRepositorySpringDataJdbcAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletsearchrepository.WalletSearchRepositorySpringDataJdbcAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletSearchRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class WalletPortsAndAdaptersConfiguration {
  @Primary
  @Bean
  fun walletSearchRepositorySpringDataJdbcAdapter(
    walletCrudRepository: WalletCrudRepository
  ): WalletSearchRepository = WalletSearchRepositorySpringDataJdbcAdapter(walletCrudRepository)

  @Bean
  fun walletRepositorySpringDataJdbcAdapter(
    walletCrudRepository: WalletCrudRepository
  ): WalletRepository = WalletRepositorySpringDataJdbcAdapter(walletCrudRepository)

  @Bean
  fun walletExistsMySQLAdapter(
    jdbcTemplate: NamedParameterJdbcTemplate
  ): WalletExists {
    return WalletExistsMySQLAdapter(jdbcTemplate)
  }
}
