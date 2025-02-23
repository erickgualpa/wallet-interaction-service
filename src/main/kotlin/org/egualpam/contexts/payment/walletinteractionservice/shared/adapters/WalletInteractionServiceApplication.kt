package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
      "org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration",
      "org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.configuration",
      "org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration",
      "org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.in.controllers",
      "org.egualpam.contexts.payment.walletinteractionservice.account.adapters.in.controllers",
      "org.egualpam.contexts.payment.walletinteractionservice.account.adapters.configuration",
    ],
)
class WalletInteractionServiceApplication

fun main(args: Array<String>) {
  runApplication<WalletInteractionServiceApplication>(*args)
}
