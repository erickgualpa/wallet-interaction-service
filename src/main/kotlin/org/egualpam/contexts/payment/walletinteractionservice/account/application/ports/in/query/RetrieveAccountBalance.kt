package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.query

import org.egualpam.contexts.payment.walletinteractionservice.account.application.domain.AccountId
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.out.AccountRepository

class RetrieveAccountBalance(
  private val repository: AccountRepository
) {

  fun execute(query: RetrieveAccountBalanceQuery): AccountBalanceDto {
    val accountId = AccountId(query.accountId)
    val account = repository.find(accountId) ?: TODO()

    val deposits = account.deposits()
    val depositsSum =
        if (deposits.isEmpty()) 0.0
        else deposits.sumOf { it.amount().value }

    val transfers = account.transfers()
    val outboundTransfers = transfers.filter { it.isOutbound() }
    val outboundTransfersSum =
        if (outboundTransfers.isEmpty()) 0.0
        else outboundTransfers.sumOf { it.amount().value }

    val inboundTransfers = transfers.filter { it.isInbound() }
    val inboundTransfersSum =
        if (inboundTransfers.isEmpty()) 0.0
        else inboundTransfers.sumOf { it.amount().value }

    val accountBalance = depositsSum - outboundTransfersSum + inboundTransfersSum

    return AccountBalanceDto(
        balance = accountBalance.toString(),
        currency = account.currency().value,
    )
  }
}

data class RetrieveAccountBalanceQuery(
  val accountId: String
)

data class AccountBalanceDto(
  val balance: String, val currency: String
)
