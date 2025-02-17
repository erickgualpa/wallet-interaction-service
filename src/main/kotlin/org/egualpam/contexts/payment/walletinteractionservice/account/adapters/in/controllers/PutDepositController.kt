package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command.DepositMoney
import org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command.DepositMoneyCommand
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidAggregateId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.AccountCurrencyIsNotSupported
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.internalServerError
import org.springframework.http.ResponseEntity.noContent
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/accounts")
@RestController
class PutDepositController(
  private val transactionTemplate: TransactionTemplate,
  private val depositMoney: DepositMoney,
) {
  private val logger: Logger = getLogger(this::class.java)

  @PutMapping("/{account-id}/deposits")
  fun putDeposit(
    @PathVariable("account-id") accountId: String,
    @RequestBody putDepositRequest: PutDepositRequest
  ): ResponseEntity<Void> {
    val command = DepositMoneyCommand(
        putDepositRequest.id,
        putDepositRequest.amount,
        putDepositRequest.currency,
        accountId,
    )
    return try {
      transactionTemplate.executeWithoutResult {
        depositMoney.execute(command)
      }
      noContent().build()
    } catch (e: RuntimeException) {
      when (e.javaClass) {
        InvalidAggregateId::class.java,
        InvalidDomainEntityId::class.java,
        AccountCurrencyIsNotSupported::class.java -> {
          logger.warn(e.message)
          badRequest().build()
        }

        else -> {
          logger.error("Unexpected error processing request [$putDepositRequest]:", e)
          internalServerError().build()
        }
      }
    }
  }
}

data class PutDepositRequest(
  val id: String,
  val amount: Double,
  val currency: String
)
