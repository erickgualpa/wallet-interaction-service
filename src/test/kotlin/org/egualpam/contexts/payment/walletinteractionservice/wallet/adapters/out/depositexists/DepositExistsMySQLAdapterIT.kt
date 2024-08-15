package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.depositexists

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.DepositId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Owner
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.ports.out.WalletRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextDouble
import kotlin.test.assertTrue

class DepositExistsMySQLAdapterIT(
  @Autowired private val walletRepository: WalletRepository,
  @Autowired private val jdbcTemplate: NamedParameterJdbcTemplate
) : AbstractIntegrationTest() {
  @Test
  fun `return true when deposit exists with given deposit id`() {
    val depositId = randomUUID().toString()
    val existingDeposit = Deposit.create(
        id = depositId,
        amount = nextDouble(10.0, 10000.0),
    )
    val existingWallet = Wallet(
        id = WalletId(randomUUID().toString()),
        owner = Owner.create(
            id = randomUUID().toString(),
            username = randomAlphabetic(5),
        ),
        accounts = mutableSetOf(
            Account.create(
                id = randomUUID().toString(),
                currency = "EUR",
                deposits = mutableSetOf(existingDeposit),
            ),
        ),
    )
    walletRepository.save(existingWallet)

    val testSubject = DepositExistsMySQLAdapter(jdbcTemplate)

    val result = testSubject.with(DepositId(depositId))
    assertTrue(result)
  }
}
