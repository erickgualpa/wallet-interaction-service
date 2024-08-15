package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc.WalletCrudRepository
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.UUID.randomUUID
import kotlin.test.assertNull

class WalletRepositorySpringDataJdbcAdapterIT(
  @Autowired private val walletCrudRepository: WalletCrudRepository,
  @Autowired private val jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractIntegrationTest() {

  @Test
  fun `return null when wallet matching id not exists`() {
    val walletId = WalletId(randomUUID().toString())
    val testSubject = WalletRepositorySpringDataJdbcAdapter(walletCrudRepository, jdbcTemplate)
    val result = testSubject.find(walletId)
    assertNull(result)
  }
}
