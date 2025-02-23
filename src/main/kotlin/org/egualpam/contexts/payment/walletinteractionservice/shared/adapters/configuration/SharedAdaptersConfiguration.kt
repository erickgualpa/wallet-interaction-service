package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.stream.Address
import com.rabbitmq.stream.Environment
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.rabbitmq.RabbitMQEventBus
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventIdSupplier
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate

@Configuration
@EnableConfigurationProperties(RabbitMqProperties::class)
class SharedAdaptersConfiguration {

  private val logger: Logger = LoggerFactory.getLogger(this::class.java)

  companion object {
    private const val PAYMENT_WALLET_STREAM_NAME = "payment.wallet"
  }

  @Bean
  fun domainEventIdSupplier(): DomainEventIdSupplier = object : DomainEventIdSupplier {
    override fun get() = DomainEventId.generate()
  }

  // TODO: Move 'EventBus' implementations into its own configuration class
  @Bean("fakeEventBus")
  fun fakeEventBus(): EventBus {
    return object : EventBus {
      override fun publish(
        domainEvents: Set<DomainEvent>
      ) = domainEvents.forEach {
        logger.info("Fake publishing of event [${it::class.java.simpleName}]")
      }
    }
  }

  @Bean
  fun eventBus(
    rabbitMqProperties: RabbitMqProperties,
    objectMapper: ObjectMapper
  ): EventBus {
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

    return RabbitMQEventBus(objectMapper, rabbitStreamTemplate)
  }
}
