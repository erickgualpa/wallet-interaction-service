package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventIdSupplier
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.`in`.command.CreateWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.`in`.query.RetrieveWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletSearchRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WalletApplicationConfiguration {
  @Bean
  fun retrieveWallet(
    walletSearchRepository: WalletSearchRepository
  ) = RetrieveWallet(walletSearchRepository)

  @Bean
  fun createWallet(
    walletExists: WalletExists,
    walletRepository: WalletRepository,
    eventBus: EventBus,
    domainEventIdSupplier: DomainEventIdSupplier
  ) = CreateWallet(walletExists, walletRepository, eventBus, domainEventIdSupplier)
}
