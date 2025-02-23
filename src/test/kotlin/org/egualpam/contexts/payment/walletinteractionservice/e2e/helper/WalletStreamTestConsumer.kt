package org.egualpam.contexts.payment.walletinteractionservice.e2e.helper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.OffsetSpecification.first

class WalletStreamTestConsumer(
  environment: Environment,
  private val objectMapper: ObjectMapper
) {

  private val consumed = mutableSetOf<String>()

  companion object {
    private const val STREAM_NAME = "payment.wallet"
  }

  init {
    environment.consumerBuilder()
        .name("test-consumer")
        .stream(STREAM_NAME)
        .offset(first())
        .messageHandler { _, message ->
          consumed.add(String(message.bodyAsBinary))
        }
        .build()
  }

  fun consume(): List<PublicEventResult> {
    return consumed.map {
      objectMapper.readValue<PublicEventResult>(it)
    }
  }
}

data class PublicEventResult(
  val id: String,
  val type: String,
  val data: Map<String, Any>
)
