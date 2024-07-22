package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("owner")
class OwnerPersistenceEntity(
  @Id val id: String?,
  @Column("entity_id") val entityId: String,
  @Column("wallet_entity_id") val walletEntityId: String,
  @Column("username") val username: String,
  @Column("created_at") val createdAt: Instant,
) {
  companion object {
    fun from(wallet: Wallet) = OwnerPersistenceEntity(
        null,
        wallet.getOwnerId().value,
        wallet.getId().value,
        wallet.getOwnerUsername().value,
        Instant.now(),
    )
  }
}
