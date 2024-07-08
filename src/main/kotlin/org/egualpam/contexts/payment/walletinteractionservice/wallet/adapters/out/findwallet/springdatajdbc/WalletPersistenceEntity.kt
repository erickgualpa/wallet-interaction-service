package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.springdatajdbc

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("wallet")
class WalletPersistenceEntity(
  @Id val id: String,
  @Column("owner_id") val ownerId: String,
  @Column("account_id") val accountId: String,
  @Column("created_at") val createdAt: Instant
)
