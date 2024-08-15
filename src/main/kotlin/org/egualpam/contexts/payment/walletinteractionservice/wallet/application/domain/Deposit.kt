package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEntity

class Deposit private constructor(
  private val id: DepositId,
  private val amount: DepositAmount
) : DomainEntity() {
  override fun getId() = id

  companion object {
    fun create(
      id: String,
      amount: Double
    ) = Deposit(
        DepositId(id),
        DepositAmount(amount),
    )
  }

  fun amount() = amount
}
