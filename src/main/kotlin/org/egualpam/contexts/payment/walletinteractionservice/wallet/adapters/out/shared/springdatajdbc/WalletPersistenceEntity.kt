package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Account
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Deposit
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Owner
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.WalletId
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.WalletDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("wallet")
class WalletPersistenceEntity(
  @Id val id: String?,
  @Column("entity_id") val entityId: String,
  @Column("created_at") val createdAt: Instant,
  private val owner: OwnerPersistenceEntity,
  private val accounts: Set<AccountPersistenceEntity>
) {
  fun toDomainEntity() = Wallet(
      id = WalletId(this.entityId),
      owner = this.owner.let {
        Owner.create(it.entityId, it.username)
      },
      accounts = this.accounts.map {
        Account.create(
            it.entityId,
            it.currency,
            it.deposits.map { d -> Deposit.create(d.entityId, d.amount) }.toMutableSet(),
        )
      }.toMutableSet(),
  )

  fun toWalletDto() = WalletDto(
      id = this.entityId,
      owner = WalletDto.OwnerDto(this.owner.entityId),
      accounts = this.accounts.map { a -> WalletDto.AccountDto(a.entityId) }.toSet(),
  )
}
