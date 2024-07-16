package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.command.CreateWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.command.CreateWalletCommand
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
    val walletId = postWalletRequest.wallet.id
    val createWalletCommand = CreateWalletCommand(
        walletId,
        postWalletRequest.wallet.owner.id,
        postWalletRequest.wallet.owner.username,
        postWalletRequest.wallet.account.id,
        postWalletRequest.wallet.account.currency,
    )
    createWallet.execute(createWalletCommand)
    return ResponseEntity.created(URI("/v1/wallet/$walletId")).build()
  }
}
