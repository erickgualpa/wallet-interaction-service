package org.egualpam.contexts.payment.walletinteractionservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WalletInteractionServiceApplication

fun main(args: Array<String>) {
	runApplication<WalletInteractionServiceApplication>(*args)
}
