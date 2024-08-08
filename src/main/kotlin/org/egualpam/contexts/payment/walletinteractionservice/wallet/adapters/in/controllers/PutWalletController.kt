package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidAggregateId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.OwnerUsernameAlreadyExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command.CreateWallet
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.internalServerError
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.status
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/wallets")
@RestController
class PutWalletController(
  private val transactionTemplate: TransactionTemplate,
  private val createWallet: CreateWallet
) {
  private val logger: Logger = getLogger(this::class.java)

  @PutMapping
  fun putWallet(@RequestBody putWalletRequest: PutWalletRequest): ResponseEntity<Void> {
    return try {
      val createWalletCommand = putWalletRequest.toCommand()
      transactionTemplate.executeWithoutResult {
        createWallet.execute(createWalletCommand)
      }
      noContent().build()
    } catch (e: RuntimeException) {
      return when (e.javaClass) {
        InvalidAggregateId::class.java, InvalidDomainEntityId::class.java -> {
          logger.warn(e.message)
          badRequest().build()
        }

        OwnerUsernameAlreadyExists::class.java -> {
          logger.warn(e.message)
          status(CONFLICT).build()
        }

        else -> internalServerError().build()
      }
    }
  }
}
