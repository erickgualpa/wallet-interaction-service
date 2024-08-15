package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface WalletCrudRepository : CrudRepository<WalletPersistenceEntity, String> {
  fun findByEntityId(entityId: String): WalletPersistenceEntity?

  @Modifying
  @Query(
      value = """
      INSERT IGNORE INTO wallet(entity_id, created_at)
      VALUES(:entityId, :createdAt)
      """,
  )
  fun saveWallet(
    @Param("entityId") entityId: String,
    @Param("createdAt") createdAt: Instant
  )

  @Modifying
  @Query(
      value = """
      INSERT IGNORE INTO owner(entity_id, created_at, username, wallet_entity_id, wallet)
      SELECT :entityId, :createdAt, :username, :walletEntityId, id
      FROM wallet
      WHERE entity_id=:walletEntityId
      """,
  )
  fun saveOwner(
    @Param("entityId") entityId: String,
    @Param("createdAt") createdAt: Instant,
    @Param("username") username: String,
    @Param("walletEntityId") walletEntityId: String
  )
}
