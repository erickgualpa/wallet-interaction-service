package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletrepository.springjdbccore

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.UUID.randomUUID
import kotlin.test.assertNull

class SpringJdbcCoreWalletRepositoryIT(
  @Autowired private val jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractIntegrationTest() {

  @Test
  fun `return null when wallet matching id not exists`() {
    val walletId = WalletId(randomUUID().toString())
    val testSubject = SpringJdbcCoreWalletRepository(jdbcTemplate)
    val result = testSubject.find(walletId)
    assertNull(result)
  }
}