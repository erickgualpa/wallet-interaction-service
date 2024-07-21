package org.egualpam.contexts.payment.walletinteractionservice.e2e

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.UUID.randomUUID
import kotlin.random.Random.Default.nextInt

class RetrieveWalletFeature : AbstractIntegrationTest() {

  @Autowired
  private lateinit var jdbcTemplate: NamedParameterJdbcTemplate

  @Test
  fun `retrieve wallet`() {
    val walletPersistenceId = nextInt(100, 999)
    val walletEntityId = randomUUID().toString()
    val accountId = randomUUID().toString()

    createWallet(walletPersistenceId, walletEntityId, accountId)

    val ownerId = randomUUID().toString()
    val ownerUsername = randomAlphabetic(10)

    createOwner(ownerId, ownerUsername, walletPersistenceId, walletEntityId)

    mockMvc.perform(get("/v1/wallets/{wallet-id}", walletEntityId))
        .andExpect(status().isOk)
        .andExpect(content().contentType("application/json"))
        .andExpect(
            content().json(
                """
                  {
                    "wallet": {
                      "id": "$walletEntityId",
                      "owner": {
                        "id": "$ownerId"
                      },
                      "account": {
                        "id": "$accountId"
                      }
                    }
                  }
                """,
            ),
        )
  }

  @Test
  fun `get 404 NOT FOUND when wallet matching wallet id not exists`() {
    val walletId = randomUUID().toString()

    mockMvc.perform(get("/v1/wallets/{wallet-id}", walletId))
        .andExpect(status().isNotFound)
  }

  private fun createWallet(persistenceId: Int, entityId: String, accountId: String) {
    val sql = """
        INSERT INTO wallet(id, entity_id, account_id, created_at)
        VALUES(:persistenceId, :entityId, :accountId, :createdAt)
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("persistenceId", persistenceId)
    sqlParameters.addValue("entityId", entityId)
    sqlParameters.addValue("accountId", accountId)
    sqlParameters.addValue("createdAt", Instant.now())

    jdbcTemplate.update(sql, sqlParameters)
  }

  private fun createOwner(
    ownerEntityId: String,
    ownerUsername: String,
    walletPersistenceId: Int,
    walletEntityId: String,
  ) {
    val sql = """
        INSERT INTO owner(entity_id, username, wallet, wallet_entity_id, created_at)
        VALUES(:ownerEntityId, :username, :walletPersistenceId, :walletEntityId, :createdAt)
      """

    val sqlParameters = MapSqlParameterSource()
    sqlParameters.addValue("ownerEntityId", ownerEntityId)
    sqlParameters.addValue("username", ownerUsername)
    sqlParameters.addValue("walletPersistenceId", walletPersistenceId)
    sqlParameters.addValue("walletEntityId", walletEntityId)
    sqlParameters.addValue("createdAt", Instant.now())

    jdbcTemplate.update(sql, sqlParameters)
  }
}
