package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.rabbitmq

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RabbitMQEventBus : EventBus {
  private val logger: Logger = LoggerFactory.getLogger(this::class.java)

  override fun publish(domainEvents: Set<DomainEvent>) {
    domainEvents.forEach {
      logger.info("Fake RabbitMQ publishing of event [${it.javaClass.simpleName}] with id [${it.id().value}]")
    }
  }
}
