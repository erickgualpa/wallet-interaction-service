package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.depositexists.DepositExistsMySQLAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletexists.WalletExistsMySQLAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository.springjdbccore.SpringJdbcCoreWalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletsearchrepository.springjdbccore.SpringJdbcCoreWalletSearchRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.DepositExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletSearchRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@Configuration
class WalletPortsAndAdaptersConfiguration {
  @Bean
  fun springJdbcCoreWalletSearchRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
  ): WalletSearchRepository = SpringJdbcCoreWalletSearchRepository(jdbcTemplate)

  @Bean
  fun springJdbcCoreWalletRepository(
    jdbcTemplate: NamedParameterJdbcTemplate
  ): WalletRepository = SpringJdbcCoreWalletRepository(
      jdbcTemplate,
  )

  @Bean
  fun walletExistsMySQLAdapter(
    jdbcTemplate: NamedParameterJdbcTemplate
  ): WalletExists {
    return WalletExistsMySQLAdapter(jdbcTemplate)
  }

  @Bean
  fun depositExistsMySQLAdapter(
    jdbcTemplate: NamedParameterJdbcTemplate
  ): DepositExists = DepositExistsMySQLAdapter(jdbcTemplate)

  @Bean
  fun fakeEventBus(): EventBus {
    return object : EventBus {
      private val logger: Logger = LoggerFactory.getLogger(this::class.java)
      override fun publish(domainEvents: Set<DomainEvent>) {
        domainEvents.forEach {
          logger.info("Fake publishing of event [${it.javaClass.simpleName}] with id [${it.id().value}]")
        }
      }
    }
  }
}
