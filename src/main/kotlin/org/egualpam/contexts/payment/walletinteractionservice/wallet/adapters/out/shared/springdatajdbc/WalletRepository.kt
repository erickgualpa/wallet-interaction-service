package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : CrudRepository<WalletPersistenceEntity, String> {
  fun findByEntityId(entityId: String): List<WalletPersistenceEntity>
}
