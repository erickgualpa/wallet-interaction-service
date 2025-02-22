package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import com.rabbitmq.stream.Address
import com.rabbitmq.stream.Environment
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.rabbitmq.RabbitMQEventBus
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventIdSupplier
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate

@Configuration
@EnableConfigurationProperties(RabbitMqProperties::class)
class SharedAdaptersConfiguration {

  companion object {
    private const val PAYMENT_WALLET_STREAM_NAME = "payment.wallet"
  }

  @Bean
  fun domainEventIdSupplier(): DomainEventIdSupplier = object : DomainEventIdSupplier {
    override fun get() = DomainEventId.generate()
  }

  @Bean
  fun eventBus(rabbitMqProperties: RabbitMqProperties): EventBus {
    // TODO: Check if 'Environment' should be a bean
    val environment = Environment.builder()
        .addressResolver {
          Address(rabbitMqProperties.host, rabbitMqProperties.streamPort)
        }
        .username(rabbitMqProperties.adminUsername)
        .password(rabbitMqProperties.adminPassword)
        .build()

    environment.streamCreator()
        .stream(PAYMENT_WALLET_STREAM_NAME)
        .create()

    val rabbitStreamTemplate = RabbitStreamTemplate(environment, PAYMENT_WALLET_STREAM_NAME)

    return RabbitMQEventBus(rabbitStreamTemplate)
  }
}
