package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("deposit")
class DepositPersistenceEntity(
  @Id val id: String?,
  @Column("entity_id") val entityId: String,
  @Column("account_entity_id") val accountEntityId: String,
  @Column("amount") val amount: Double,
  @Column("created_at") val createdAt: Instant
)
