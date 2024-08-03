package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidAggregateId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command.CreateWallet
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/wallets")
@RestController
class PutWalletController(
  private val transactionalCreateWallet: CreateWallet
) {

  @PutMapping
  fun putWallet(@RequestBody putWalletRequest: PutWalletRequest): ResponseEntity<Void> {
    return try {
      val createWalletCommand = putWalletRequest.toCommand()
      transactionalCreateWallet.execute(createWalletCommand)
      ResponseEntity.noContent().build()
    } catch (e: InvalidAggregateId) {
      ResponseEntity.badRequest().build()
    } catch (e: InvalidDomainEntityId) {
      ResponseEntity.badRequest().build()
    }
  }
}
