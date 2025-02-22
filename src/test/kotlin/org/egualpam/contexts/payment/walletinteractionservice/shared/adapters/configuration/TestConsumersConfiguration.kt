package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.stream.Address
import com.rabbitmq.stream.Environment
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.WalletStreamTestConsumer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestConsumersConfiguration {
  @Bean
  fun walletStreamTestConsumer(
    rabbitMqProperties: RabbitMqProperties,
    objectMapper: ObjectMapper
  ): WalletStreamTestConsumer {
    val environment = Environment.builder()
        .addressResolver {
          Address(rabbitMqProperties.host, rabbitMqProperties.streamPort)
        }
        .username(rabbitMqProperties.adminUsername)
        .password(rabbitMqProperties.adminPassword)
        .build()

    return WalletStreamTestConsumer(environment)
  }
}
