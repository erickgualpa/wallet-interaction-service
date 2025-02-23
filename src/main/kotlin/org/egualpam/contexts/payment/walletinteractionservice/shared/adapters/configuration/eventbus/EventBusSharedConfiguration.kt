package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration.eventbus

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.stream.Address
import com.rabbitmq.stream.Environment
import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.springrabbit.SpringRabbitEventBus
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate

@Configuration
@EnableConfigurationProperties(RabbitMqProperties::class)
class EventBusSharedConfiguration {
  private val logger: Logger = LoggerFactory.getLogger(this::class.java)

  companion object {
    private const val PAYMENT_WALLET_STREAM_NAME = "payment.wallet"
  }

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
  fun rabbitMqEnvironment(
    rabbitMqProperties: RabbitMqProperties
  ): Environment {
    return Environment.builder()
        .addressResolver {
          Address(rabbitMqProperties.host, rabbitMqProperties.streamPort)
        }
        .username(rabbitMqProperties.adminUsername)
        .password(rabbitMqProperties.adminPassword)
        .build()
  }

  @Bean
  fun eventBus(
    rabbitMqEnvironment: Environment,
    objectMapper: ObjectMapper
  ): EventBus {
    rabbitMqEnvironment.streamCreator()
        .stream(PAYMENT_WALLET_STREAM_NAME)
        .create()

    val rabbitStreamTemplate = RabbitStreamTemplate(
        rabbitMqEnvironment,
        PAYMENT_WALLET_STREAM_NAME,
    )

    return SpringRabbitEventBus(objectMapper, rabbitStreamTemplate)
  }
}
