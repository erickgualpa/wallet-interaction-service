package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletFakeAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletMySQLAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletSpringDataJdbcAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.savewallet.SaveWalletSpringDataJdbcAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExistsPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class WalletPortsAndAdaptersConfiguration {
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
  fun saveWalletSpringDataJdbcAdapter(
    walletRepository: WalletRepository
  ): SaveWalletPort = SaveWalletSpringDataJdbcAdapter(walletRepository)

  @Bean
  fun walletExistsFakeAdapter(): WalletExistsPort {
    return object : WalletExistsPort {
      override fun exists(walletId: WalletId) = false
    }
  }
}
