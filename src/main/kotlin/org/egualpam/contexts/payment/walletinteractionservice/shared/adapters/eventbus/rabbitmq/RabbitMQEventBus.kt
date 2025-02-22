package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.rabbitmq

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate

// TODO: Rename this to SpringRabbitEventBus
class RabbitMQEventBus(
  private val rabbitStreamTemplate: RabbitStreamTemplate
) : EventBus {

  private val logger: Logger = LoggerFactory.getLogger(this::class.java)

  override fun publish(domainEvents: Set<DomainEvent>) {
    domainEvents.forEach {
      val content =
          "Fake domain event [${it.javaClass.simpleName}] with id [${it.id().value}]"

      val message = rabbitStreamTemplate.messageBuilder()
          .addData(content.toByteArray())
          .build()

      rabbitStreamTemplate.send(message)
      logger.info("Event [${it.javaClass.simpleName}] has been published")
    }
  }
}
