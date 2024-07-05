package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.RetrieveWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.RetrieveWalletQuery
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/wallets")
@RestController
class GetWalletController(
  private var retrieveWallet: RetrieveWallet
) {

  @GetMapping("/{wallet-id}")
  fun getWallet(@PathVariable("wallet-id") walletId: String): ResponseEntity<GetWalletResponse> {
    val walletDto = retrieveWallet.execute(RetrieveWalletQuery(walletId))
    return ResponseEntity.ok(GetWalletResponse.from(walletDto))
  }
}
