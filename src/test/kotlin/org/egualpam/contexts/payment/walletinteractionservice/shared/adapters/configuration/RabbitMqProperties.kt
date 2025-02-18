package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "message-broker.rabbitmq")
data class RabbitMqProperties(
  val host: String,
  val amqpPort: Int,
  val adminUsername: String,
  val adminPassword: String
)
