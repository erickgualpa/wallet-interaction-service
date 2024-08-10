package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("account")
class AccountPersistenceEntity(
  @Id val id: String?,
  @Column("entity_id") val entityId: String,
  @Column("wallet_entity_id") val walletEntityId: String,
  @Column("currency") val currency: String,
  @Column("created_at") val createdAt: Instant,
)
