package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.springdatajdbc

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.WalletId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID.randomUUID
import kotlin.test.assertNull

class FindWalletSpringDataJdbcAdapterIT : AbstractIntegrationTest() {

  @Autowired
  private lateinit var walletRepository: WalletRepository

  @Test
  fun `return null when wallet matching it not exists`() {
    val walletId = WalletId(randomUUID().toString())
    val testSubject = FindWalletSpringDataJdbcAdapter(walletRepository)
    val result = testSubject.find(walletId)
    assertNull(result)
  }
}
