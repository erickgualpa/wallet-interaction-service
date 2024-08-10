package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.Wallet
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
  private val persistenceOwner: OwnerPersistenceEntity,
  private val persistenceAccounts: Set<AccountPersistenceEntity>
) {
  companion object {
    fun from(wallet: Wallet) = WalletPersistenceEntity(
        id = null,
        entityId = wallet.getId().value,
        createdAt = now(),
        persistenceOwner = OwnerPersistenceEntity.from(wallet),
        persistenceAccounts = wallet.accounts().map {
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

  fun ownerId() = this.persistenceOwner.entityId

  // TODO: This is a workaround. It should be updated
  fun accountId() = this.persistenceAccounts.first().entityId
}
