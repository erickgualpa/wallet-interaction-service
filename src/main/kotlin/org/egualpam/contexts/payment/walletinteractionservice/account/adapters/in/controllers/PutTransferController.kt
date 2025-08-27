package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.TransferMoney
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.TransferMoneyCommand
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
import java.util.concurrent.locks.ReentrantLock

@RequestMapping("/v1/accounts")
@RestController
class PutTransferController(
  private val transactionTemplate: TransactionTemplate,
  private val transferMoney: TransferMoney
) {
  private val logger: Logger = getLogger(this::class.java)
  private val lock = ReentrantLock()

  @PutMapping("/{account-id}/transfers")
  fun putTransfer(
    @PathVariable("account-id") accountId: String,
    @RequestBody request: PutTransferRequest
  ): ResponseEntity<Void> {
    val command = TransferMoneyCommand(
        transferId = request.id,
        sourceAccountId = accountId,
        destinationAccountId = request.destinationAccountId,
        amount = request.amount,
    )

    return try {
      lock.lock()
      transactionTemplate.executeWithoutResult {
        transferMoney.execute(command)
      }
      lock.unlock()
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
          logger.error("Unexpected error processing request [$request]:", e)
          internalServerError().build()
        }
      }
    }
  }
}

data class PutTransferRequest(
  val id: String,
  val destinationAccountId: String,
  val amount: Double
)
