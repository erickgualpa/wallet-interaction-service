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
  @Column("account_id") val accountId: String,
  @Column("created_at") val createdAt: Instant,
  private val ownerPersistenceEntity: OwnerPersistenceEntity
) {
  companion object {
    fun from(wallet: Wallet) = WalletPersistenceEntity(
        null,
        wallet.getId().value,
        wallet.getAccountId().value,
        Instant.now(),
        OwnerPersistenceEntity.from(wallet),
    )
  }

  fun ownerId(): String {
    return this.ownerPersistenceEntity.entityId
  }
}
