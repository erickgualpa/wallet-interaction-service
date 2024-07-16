package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.command.CreateWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWalletPort
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.query.RetrieveWallet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WalletApplicationConfiguration {
  @Bean
  fun retrieveWallet(findWalletPort: FindWalletPort) = RetrieveWallet(findWalletPort)

  @Bean
  fun createWallet(saveWalletPort: SaveWalletPort) = CreateWallet(saveWalletPort)
}
