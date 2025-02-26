package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.`in`.consumers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.OffsetSpecification.next
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.CreateAccount
import org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command.CreateAccountCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.transaction.support.TransactionTemplate

class CreateAccountConsumer(
  environment: Environment,
  private val objectMapper: ObjectMapper,
  private val transactionTemplate: TransactionTemplate,
  private val createAccount: CreateAccount,
) {

  private val logger: Logger = LoggerFactory.getLogger(this::class.java)

  companion object {
    private const val STREAM_NAME = "payment.wallet"
  }

  init {
    environment.consumerBuilder()
        .name("create-account-consumer")
        .stream(STREAM_NAME)
        .offset(next())
        .messageHandler { _, message ->
          val event = objectMapper.readValue<WalletCreatedEvent>(message.bodyAsBinary)

          val command = CreateAccountCommand(
              event.data["accountId"] as String,
              event.data["accountCurrency"] as String,
              walletId = event.aggregateId,
          )

          try {
            transactionTemplate.executeWithoutResult {
              createAccount.execute(command)
            }
          } catch (e: RuntimeException) {
            logger.error("Unexpected error processing event [$event]:", e)
          }
        }
        .build()
  }

  private data class WalletCreatedEvent(
    val id: String,
    val type: String,
    val version: String,
    val aggregateId: String,
    val data: Map<String, Any>
  )
}
