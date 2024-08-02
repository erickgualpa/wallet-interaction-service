package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.FindWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.SaveWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command.CreateWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.RetrieveWallet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WalletApplicationConfiguration {
  @Bean
  fun retrieveWallet(findWallet: FindWallet) = RetrieveWallet(findWallet)

  @Bean
  fun createWallet(
    walletExists: WalletExists,
    saveWallet: SaveWallet
  ) = CreateWallet(walletExists, saveWallet)
}
