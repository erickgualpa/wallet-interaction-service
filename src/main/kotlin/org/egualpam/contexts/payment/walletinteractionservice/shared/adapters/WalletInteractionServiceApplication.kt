package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
      "org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration",
      "org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration",
      "org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.in.controllers",
    ],
)
class WalletInteractionServiceApplication

fun main(args: Array<String>) {
  runApplication<WalletInteractionServiceApplication>(*args)
}
