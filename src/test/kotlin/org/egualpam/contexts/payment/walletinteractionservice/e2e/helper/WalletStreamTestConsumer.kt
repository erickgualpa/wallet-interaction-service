package org.egualpam.contexts.payment.walletinteractionservice.e2e.helper

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.OffsetSpecification.first

class WalletStreamTestConsumer(
  environment: Environment
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

  fun consume(): String {
    return consumed.first()
  }
}
