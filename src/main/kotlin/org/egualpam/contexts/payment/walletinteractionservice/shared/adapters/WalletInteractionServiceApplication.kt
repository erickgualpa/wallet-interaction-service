package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@EnableJdbcRepositories(
    "org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.springdatajdbc",
)
@SpringBootApplication(
    scanBasePackages = [
      "org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.configuration",
      "org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.in.controllers",
    ],
)
class WalletInteractionServiceApplication

fun main(args: Array<String>) {
  runApplication<WalletInteractionServiceApplication>(*args)
}
