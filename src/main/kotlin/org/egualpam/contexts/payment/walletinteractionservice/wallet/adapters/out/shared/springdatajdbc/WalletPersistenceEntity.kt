package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("wallet")
class WalletPersistenceEntity(
  @Id val id: String?,
  @Column("entity_id") val entityId: String,
  @Column("created_at") val createdAt: Instant,
  private val ownerPersistenceEntity: OwnerPersistenceEntity,
  private val accountPersistenceEntity: AccountPersistenceEntity
) {
  companion object {
    fun from(wallet: Wallet) = WalletPersistenceEntity(
        null,
        wallet.getId().value,
        Instant.now(),
        OwnerPersistenceEntity.from(wallet),
        AccountPersistenceEntity.from(wallet),
    )
  }

  fun ownerId() = this.ownerPersistenceEntity.entityId

  fun accountId() = this.accountPersistenceEntity.entityId
}
