package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.FindWalletFakeAdapter
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.RetrieveWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WalletConfiguration {
  @Bean
  fun findWalletPort(): FindWalletPort = FindWalletFakeAdapter()

  @Bean
  fun retrieveWallet(findWalletPort: FindWalletPort) = RetrieveWallet(findWalletPort)
}
