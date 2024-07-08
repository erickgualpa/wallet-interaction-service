package org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.findwallet.springdatajdbc

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : CrudRepository<WalletPersistenceEntity, String>
