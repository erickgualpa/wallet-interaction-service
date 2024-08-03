package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletFakeAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletMySQLAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.FindWalletSpringDataJdbcAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.savewallet.SaveWalletSpringDataJdbcAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command.CreateWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command.CreateWalletCommand
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.support.TransactionTemplate

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
    walletRepository: WalletRepository
  ): FindWallet = FindWalletSpringDataJdbcAdapter(walletRepository)

  @Bean
  fun saveWalletSpringDataJdbcAdapter(
    walletRepository: WalletRepository
  ): SaveWallet = SaveWalletSpringDataJdbcAdapter(walletRepository)

  @Bean
  fun walletExistsFakeAdapter(): WalletExists {
    return object : WalletExists {
      override fun with(walletId: WalletId) = false
    }
  }

  @Bean("transactionalCreateWallet")
  fun transactionalCreateWallet(
    transactionalTemplate: TransactionTemplate,
    walletExists: WalletExists,
    saveWallet: SaveWallet
  ): CreateWallet {
    return object : CreateWallet(walletExists, saveWallet) {
      override fun execute(createWalletCommand: CreateWalletCommand) {
        transactionalTemplate.executeWithoutResult {
          super.execute(createWalletCommand)
        }
      }
    }
  }
}
