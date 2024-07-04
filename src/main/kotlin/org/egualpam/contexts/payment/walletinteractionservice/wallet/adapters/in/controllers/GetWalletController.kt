package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/wallets")
@RestController
class GetWalletController {

  @GetMapping("/{wallet-id}")
  fun getWallet(@PathVariable("wallet-id") walletId: String): ResponseEntity<GetWalletResponse> {
    return ResponseEntity.ok(
        GetWalletResponse(
            GetWalletResponse.Wallet(
                walletId,
                GetWalletResponse.Owner("fake-owner-id"),
                GetWalletResponse.Account("fake-account-id"),
            ),
        ),
    )
  }
}
