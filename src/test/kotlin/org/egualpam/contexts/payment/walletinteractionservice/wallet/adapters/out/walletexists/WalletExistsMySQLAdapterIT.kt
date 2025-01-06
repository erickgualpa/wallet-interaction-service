package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.walletexists

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventIdSupplier
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.OwnerUsername
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.UUID.randomUUID
import kotlin.test.assertTrue

class WalletExistsMySQLAdapterIT : AbstractIntegrationTest() {

  @Autowired
  private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

  @Autowired
  private lateinit var domainEventIdSupplier: DomainEventIdSupplier

  @Autowired
  private lateinit var walletRepository: WalletRepository

  @Test
  fun `return true when a wallet with given wallet id exists`() {
    val walletId = WalletId(randomUUID().toString())
    val wallet = Wallet.create(
        walletId.value,
        ownerId = randomUUID().toString(),
        ownerUsername = randomAlphabetic(5),
        accountId = randomUUID().toString(),
        accountCurrency = "EUR",
        domainEventId = domainEventIdSupplier.get(),
    )

    walletRepository.save(wallet)

    val testSubject = WalletExistsMySQLAdapter(jdbcTemplate)
    assertTrue {
      testSubject.with(walletId)
    }
  }

  @Test
  fun `return true when a wallet with given owner username exists`() {
    val ownerUsername = OwnerUsername(randomAlphabetic(5))
    val wallet = Wallet.create(
        id = randomUUID().toString(),
        ownerId = randomUUID().toString(),
        ownerUsername.value,
        accountId = randomUUID().toString(),
        accountCurrency = "EUR",
        domainEventId = domainEventIdSupplier.get(),
    )

    walletRepository.save(wallet)

    val testSubject = WalletExistsMySQLAdapter(jdbcTemplate)
    assertTrue {
      testSubject.with(ownerUsername)
    }
  }
}
