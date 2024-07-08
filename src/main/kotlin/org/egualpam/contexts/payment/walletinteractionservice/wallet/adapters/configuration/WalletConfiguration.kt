package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletFakeAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletMySQLAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.springdatajdbc.FindWalletSpringDataJdbcAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.springdatajdbc.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.query.RetrieveWallet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class WalletConfiguration {
  @Bean
  fun findWalletFakeAdapter(): FindWalletPort = FindWalletFakeAdapter()

  @Bean
  fun findWalletMySQLAdapter(
    jdbcTemplate: NamedParameterJdbcTemplate
  ): FindWalletPort = FindWalletMySQLAdapter(jdbcTemplate)

  @Primary
  @Bean
  fun findWalletSpringDataJdbcAdapter(
    walletRepository: WalletRepository
  ): FindWalletPort = FindWalletSpringDataJdbcAdapter(walletRepository)

  @Bean
  fun retrieveWallet(findWalletPort: FindWalletPort) = RetrieveWallet(findWalletPort)
}
