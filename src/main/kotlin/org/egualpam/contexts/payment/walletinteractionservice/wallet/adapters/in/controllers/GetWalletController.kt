package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidAggregateId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletNotExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.RetrieveWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.RetrieveWalletQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.internalServerError
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/wallets")
@RestController
class GetWalletController(
  private var retrieveWallet: RetrieveWallet
) {
  private val logger: Logger = getLogger(this::class.java)

  @GetMapping("/{wallet-id}")
  fun getWallet(@PathVariable("wallet-id") walletId: String): ResponseEntity<GetWalletResponse> {
    return try {
      val walletDto = RetrieveWalletQuery(walletId).let {
        retrieveWallet.execute(it)
      }
      ok(GetWalletResponse.from(walletDto))
    } catch (e: RuntimeException) {
      return when (e.javaClass) {
        InvalidAggregateId::class.java, WalletNotExists::class.java -> {
          logger.warn(e.message)
          notFound().build()
        }

        else -> {
          logger.error("Unexpected error processing request:")
          internalServerError().build()
        }
      }
    }
  }
}
