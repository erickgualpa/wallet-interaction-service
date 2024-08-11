package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/wallets")
@RestController
class PutWalletDeposit {
  @PutMapping("/{wallet-id}/deposit")
  fun putWalletDeposit(): ResponseEntity<Void> {
    return noContent().build()
  }
}
