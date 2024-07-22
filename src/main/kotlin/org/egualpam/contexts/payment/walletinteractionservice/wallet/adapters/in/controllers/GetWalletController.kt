package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.`in`.controllers

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions.InvalidDomainEntityId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.WalletNotExists
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.RetrieveWallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.RetrieveWalletQuery
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
    return try {
      val walletDto = RetrieveWalletQuery(walletId).let {
        retrieveWallet.execute(it)
      }
      ResponseEntity.ok(GetWalletResponse.from(walletDto))
    } catch (e: InvalidDomainEntityId) {
      ResponseEntity.notFound().build()
    } catch (e: WalletNotExists) {
      ResponseEntity.notFound().build()
    }
  }
}
