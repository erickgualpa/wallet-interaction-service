package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command.DepositMoney
import org.egualpam.contexts.payment.walletinteractionservice.account.application.usecases.command.DepositMoneyCommand
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/accounts")
@RestController
class PutDepositController(
  private val depositMoney: DepositMoney
) {

  @PutMapping("/{account-id}/deposits")
  fun putDeposit(
    @PathVariable("account-id") accountId: String,
    @RequestBody putDepositRequest: PutDepositRequest
  ): ResponseEntity<Void> {
    val command = DepositMoneyCommand(
        putDepositRequest.id,
        putDepositRequest.amount,
        putDepositRequest.currency,
        putDepositRequest.accountId,
    )
    depositMoney.execute(command)
    return noContent().build();
  }
}

data class PutDepositRequest(
  val id: String,
  val amount: Double,
  val currency: String,
  val accountId: String
)
