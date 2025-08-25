package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.out.accountrepository.springjdbccore

import org.assertj.core.api.Assertions.assertThat
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.Transfer
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.support.TransactionTemplate
import java.util.UUID.randomUUID

class SpringJdbcCoreAccountRepositoryIT : AbstractIntegrationTest() {

  @Autowired
  private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

  @Autowired
  private lateinit var transactionTemplate: TransactionTemplate

  companion object {
    private const val CURRENCY = "EUR"
  }

  @Test
  fun `find account when is saved`() {
    val walletId = randomUUID().toString()
    val accountId = randomUUID().toString()

    transactionTemplate.executeWithoutResult { _ ->
      walletTestRepository.createWallet(walletId)
    }

    val testSubject: AccountRepository = SpringJdbcCoreAccountRepository(jdbcTemplate)

    val deposit = Deposit.load(
        id = randomUUID().toString(),
        amount = 100.0,
    )

    val account = Account.load(
        accountId,
        walletId,
        CURRENCY,
        deposits = mutableSetOf(deposit),
        transfers = mutableSetOf(),
    )

    transactionTemplate.executeWithoutResult { _ ->
      testSubject.save(account)
    }

    val result = testSubject.find(AccountId(accountId))

    assertThat(result).usingRecursiveComparison().isEqualTo(account)
  }

  @Test
  fun `find account when has transfers`() {
    val sourceWalletId = randomUUID().toString()
    val sourceAccountId = randomUUID().toString()

    val destinationWalletId = randomUUID().toString()
    val destinationAccountId = randomUUID().toString()

    val transferId = randomUUID().toString()

    transactionTemplate.executeWithoutResult { _ ->
      walletTestRepository.createWallet(sourceWalletId)
      walletTestRepository.createWallet(destinationWalletId)
    }

    val testSubject: AccountRepository = SpringJdbcCoreAccountRepository(jdbcTemplate)

    val outboundTransfer = Transfer.load(
        id = transferId,
        sourceAccountId = sourceAccountId,
        destinationAccountId = destinationAccountId,
        amount = 100.0,
        isInbound = false,
    )

    val sourceAccount = Account.load(
        sourceAccountId,
        sourceWalletId,
        CURRENCY,
        deposits = mutableSetOf(),
        transfers = mutableSetOf(outboundTransfer),
    )

    val inboundTransfer = Transfer.load(
        id = transferId,
        sourceAccountId = sourceAccountId,
        destinationAccountId = destinationAccountId,
        amount = 100.0,
        isInbound = true,
    )

    val destinationAccount = Account.load(
        destinationAccountId,
        destinationWalletId,
        CURRENCY,
        deposits = mutableSetOf(),
        transfers = mutableSetOf(inboundTransfer),
    )

    transactionTemplate.executeWithoutResult { _ ->
      testSubject.save(sourceAccount)
      testSubject.save(destinationAccount)
    }

    val firstResult = testSubject.find(AccountId(sourceAccountId))
    val secondResult = testSubject.find(AccountId(destinationAccountId))

    assertThat(firstResult).usingRecursiveComparison().isEqualTo(sourceAccount)
    assertThat(secondResult).usingRecursiveComparison().isEqualTo(destinationAccount)
  }
}
