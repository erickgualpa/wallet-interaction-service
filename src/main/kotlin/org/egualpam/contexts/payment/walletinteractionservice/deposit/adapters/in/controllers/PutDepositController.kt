package org.egualpam.contexts.payment.walletinteractionservice.deposit.adapters.`in`.controllers

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/deposits")
@RestController
class PutDepositController {

  @PutMapping
  fun putDeposit(@RequestBody putDepositRequest: PutDepositRequest): ResponseEntity<Void> {
    return noContent().build();
  }
}

data class PutDepositRequest(
  val id: String,
  val amount: Double,
  val currency: String,
  val walletId: String
)
