package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidAggregateId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.AccountCurrencyIsNotSupported
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.internalServerError
import org.springframework.http.ResponseEntity.notFound
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/wallets")
@RestController
class PutWalletDepositController {
  private val logger: Logger = getLogger(this::class.java)

  @PutMapping("/{wallet-id}/deposit")
  fun putWalletDeposit(
    @PathVariable("wallet-id") walletId: String,
    @RequestBody putWalletDepositRequest: PutWalletDepositRequest
  ): ResponseEntity<Void> {
    return try {
      notFound().build()
    } catch (e: RuntimeException) {
      // TODO: Move this into new controller
      when (e.javaClass) {
        InvalidAggregateId::class.java,
        InvalidDomainEntityId::class.java,
        AccountCurrencyIsNotSupported::class.java -> {
          logger.warn(e.message)
          badRequest().build()
        }

        else -> {
          logger.error("Unexpected error processing request [$putWalletDepositRequest]:", e)
          internalServerError().build()
        }
      }
    }
  }
}
