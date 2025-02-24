package org.egualpam.contexts.payment.walletinteractionservice.account.adapters.`in`.consumers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.OffsetSpecification.next
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CreateAccountConsumer(
  environment: Environment,
  private val objectMapper: ObjectMapper
) {

  private val logger: Logger = LoggerFactory.getLogger(this::class.java)

  companion object {
    private const val STREAM_NAME = "payment.wallet"
  }

  init {
    environment.consumerBuilder()
        .name("test-consumer")
        .stream(STREAM_NAME)
        .offset(next())
        .messageHandler { _, message ->
          val event = objectMapper.readValue<WalletCreatedEvent>(message.bodyAsBinary)
          logger.info("Event [${event.type}] has been consumed")
        }
        .build()
  }

  private data class WalletCreatedEvent(
    val id: String,
    val type: String,
    val version: String,
  )
}
