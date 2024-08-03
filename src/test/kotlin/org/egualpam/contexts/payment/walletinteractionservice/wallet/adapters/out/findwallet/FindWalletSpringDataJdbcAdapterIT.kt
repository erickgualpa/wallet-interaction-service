package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletCrudRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID.randomUUID
import kotlin.test.assertNull

class FindWalletSpringDataJdbcAdapterIT : AbstractIntegrationTest() {

  @Autowired
  private lateinit var walletCrudRepository: WalletCrudRepository

  @Test
  fun `return null when wallet matching id not exists`() {
    val walletId = WalletId(randomUUID().toString())
    val testSubject = FindWalletSpringDataJdbcAdapter(walletCrudRepository)
    val result = testSubject.find(walletId)
    assertNull(result)
  }
}
