package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEntity

class Transfer private constructor(
  private val id: TransferId,
  private val sourceAccountId: AccountId,
  private val destinationAccountId: AccountId,
  private val amount: TransferAmount,
  private val isInbound: Boolean
) : DomainEntity() {

  override fun getId() = id

  companion object {
    fun create(
      id: String,
      sourceAccountId: String,
      destinationAccountId: String,
      amount: Double,
      isInbound: Boolean,
    ) = Transfer(
        id = TransferId(id),
        sourceAccountId = AccountId(sourceAccountId),
        destinationAccountId = AccountId(destinationAccountId),
        amount = TransferAmount(amount),
        isInbound,
    )

    fun load(
      id: String,
      sourceAccountId: String,
      destinationAccountId: String,
      amount: Double,
      isInbound: Boolean
    ) = Transfer(
        id = TransferId(id),
        sourceAccountId = AccountId(sourceAccountId),
        destinationAccountId = AccountId(destinationAccountId),
        amount = TransferAmount(amount),
        isInbound,
    )
  }

  fun sourceAccountId() = sourceAccountId.value
  fun destinationAccountId() = destinationAccountId.value
  fun amount() = amount
  fun isInbound() = isInbound
  fun isOutbound() = !isInbound
}
