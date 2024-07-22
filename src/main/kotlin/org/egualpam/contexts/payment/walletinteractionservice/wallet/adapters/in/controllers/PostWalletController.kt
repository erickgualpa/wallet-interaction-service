package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.command.CreateWallet
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/v1/wallets")
@RestController
class PostWalletController(
  private val createWallet: CreateWallet
) {

  @PostMapping
  fun postWallet(@RequestBody postWalletRequest: PostWalletRequest): ResponseEntity<Void> {
    return try {
      postWalletRequest.toCommand().let {
        createWallet.execute(it)
      }
      ResponseEntity.created(URI("/v1/wallet/${postWalletRequest.wallet.id}")).build()
    } catch (e: InvalidDomainEntityId) {
      ResponseEntity.badRequest().build()
    }
  }
}
