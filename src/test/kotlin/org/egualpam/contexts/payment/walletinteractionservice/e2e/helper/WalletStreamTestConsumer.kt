package org.egualpam.contexts.payment.walletinteractionservice.e2e.helper

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Delivery
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration.RabbitMqProperties

// TODO: Refactor this code once messages can be consumed
class WalletStreamTestConsumer(
  rabbitMqProperties: RabbitMqProperties,
) {
  private val connection: Connection

  init {
    val connectionFactory = ConnectionFactory()
    connectionFactory.host = rabbitMqProperties.host
    connectionFactory.port = rabbitMqProperties.amqpPort
    connectionFactory.username = rabbitMqProperties.adminUsername
    connectionFactory.password = rabbitMqProperties.adminPassword
    connection = connectionFactory.newConnection()
  }

  companion object {
    private const val STREAM_NAME = "payment.wallet"
  }

  fun consume(): String? {
    val channel = connection.createChannel()

    val durable = true
    val notExclusive = false
    val notAutoDelete = false

    channel.queueDeclare(
        STREAM_NAME,
        durable,
        notExclusive,
        notAutoDelete,
        mapOf(Pair("x-queue-type", "stream")),
    )


    var consumed: String? = null

    channel.basicQos(100)
    channel.basicConsume(
        STREAM_NAME,
        false,
        mapOf(Pair("x-stream-offset", "first")),
        { _: String?, message: Delivery ->
          consumed = String(message.body)
          channel.basicAck(message.envelope.deliveryTag, false)
        },
        { _: String? -> },
    )

    channel.close()

    return consumed
  }
}
