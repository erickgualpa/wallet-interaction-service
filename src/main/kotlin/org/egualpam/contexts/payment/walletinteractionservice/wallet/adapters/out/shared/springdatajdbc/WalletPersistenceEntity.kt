package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.usecases.query.WalletDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.Instant.now

@Table("wallet")
class WalletPersistenceEntity(
  @Id val id: String?,
  @Column("entity_id") val entityId: String,
  @Column("created_at") val createdAt: Instant,
  private val owner: OwnerPersistenceEntity,
  private val accounts: Set<AccountPersistenceEntity>
) {
  companion object {
    fun from(wallet: Wallet) = WalletPersistenceEntity(
        id = null,
        entityId = wallet.getId().value,
        createdAt = now(),
        owner = OwnerPersistenceEntity.from(wallet),
        accounts = wallet.accounts().map {
          AccountPersistenceEntity(
              id = null,
              entityId = it.getId().value,
              walletEntityId = wallet.getId().value,
              currency = it.getCurrency().value,
              createdAt = now(),
          )
        }.toSet(),
    )
  }

  fun toWalletDto() = WalletDto(
      id = this.entityId,
      owner = WalletDto.OwnerDto(this.owner.entityId),
      accounts = this.accounts.map { a -> WalletDto.AccountDto(a.entityId) }.toSet(),
  )
}
