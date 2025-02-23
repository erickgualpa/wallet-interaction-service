package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.stream.Environment
import org.egualpam.contexts.payment.walletinteractionservice.e2e.helper.WalletStreamTestConsumer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestConsumersConfiguration {
  @Bean
  fun walletStreamTestConsumer(
    rabbitMqEnvironment: Environment,
    objectMapper: ObjectMapper
  ) = WalletStreamTestConsumer(rabbitMqEnvironment, objectMapper)
}
