package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.egualpam.contexts.payment.walletinteractionservice.wallet.domain.Wallet
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("wallet")
class WalletPersistenceEntity(
  @Id val id: String?,
  @Column("entity_id") val entityId: String,
  @Column("owner_id") val ownerId: String,
  @Column("account_id") val accountId: String,
  @Column("created_at") val createdAt: Instant
) {
  companion object {
    fun from(wallet: Wallet) = WalletPersistenceEntity(
        null,
        wallet.getId().value,
        wallet.getOwnerId().value,
        wallet.getAccountId().value,
        Instant.now(),
    )
  }
}
